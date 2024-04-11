package com.example.TicketsService.service;

import com.example.TicketsService.Factory.PurchaseFactory;
import com.example.TicketsService.Mapper.PurchaseMapper;
import com.example.TicketsService.dto.request.PurchaseAcceptRequest;
import com.example.TicketsService.dto.request.PurchaseTicketRequest;
import com.example.TicketsService.dto.response.Link;
import com.example.TicketsService.dto.response.PayPallMakePaymentResponse;
import com.example.TicketsService.dto.response.PurchaseReponse;
import com.example.TicketsService.model.PurchaseEntity;
import com.example.TicketsService.repository.PurchaseRepository;
import com.example.TicketsService.security.service.UserDetailsImpl;
import com.example.TicketsService.validate.PayPalValidator;
import jakarta.validation.ValidationException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import com.example.TicketsService.model.EventEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import java.util.Date;

@Service
public class PurchaseService {

    private final ConsumerService consumerService;
    private final EventService eventService;
    private final OrderPreparationService orderPreparationService;
    private final PayPallService payPallService;
    private final TokenCacheService tokenCacheService;
    private final PurchaseRepository purchaseRepository;
    private final PayPalValidator payPalValidator;
    private final PurchaseFactory purchaseFactory;
    private final PurchaseMapper purchaseMapper;

    @Autowired
    public PurchaseService(ConsumerService consumerService, EventService eventService, OrderPreparationService orderPreparationService, PayPallService payPallService, TokenCacheService tokenCacheService, PurchaseRepository purchaseRepository, PayPalValidator payPalValidator, PurchaseFactory purchaseFactory, PurchaseMapper purchaseMapper) {
        this.consumerService = consumerService;
        this.eventService = eventService;
        this.orderPreparationService = orderPreparationService;
        this.payPallService = payPallService;
        this.tokenCacheService = tokenCacheService;
        this.purchaseRepository = purchaseRepository;
        this.payPalValidator = payPalValidator;
        this.purchaseFactory = purchaseFactory;
        this.purchaseMapper = purchaseMapper;
    }

    private void save(PurchaseEntity purchase){
        purchaseRepository.save(purchase);
    }

    public Mono<ResponseEntity<String>> makePayment(PurchaseTicketRequest request) {

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = userDetails.getEmail();
        ObjectId consumerId = getConsumerId(userEmail);
        String jsonBodyPayment;

        EventEntity event = getEvent(request.getEventId());
        Date currentDate = new Date();
        payPalValidator.validate3Args(event, currentDate, request.getTickets());

        try{
            event.setTicketsBought(event.getTicketsBought() + request.getTickets());
            eventService.save(event);
        }catch (Exception e){
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error while blocking ticket quantity for 15 minutes: " + e.getMessage()));
        }

        try{
            jsonBodyPayment = orderPreparationService.generateOrderPayPallData(event, request.getTickets());
        }catch (Exception e){
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Server error, try again soon."));
        }

        String bererToken = tokenCacheService.getToken();
        payPalValidator.validate(bererToken);


            return payPallService.makePayment(bererToken, jsonBodyPayment)
                    .flatMap(response -> createEntityAndSaveToDatabase(request, response, currentDate, consumerId))
                    .onErrorResume(error -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment failed: " + error.getMessage())))
                    .defaultIfEmpty(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error with payment!"));
    }
    public EventEntity getEvent(String eventId) {
        return eventService.getEventEntityByEventId(new ObjectId(eventId))
                .orElseThrow(() -> new RuntimeException("Brak wydarzenia o podanym id!"));
    }
    public ObjectId getConsumerId(String userEmail) {
        return consumerService.getConsumerIdByEmail(userEmail);
    }

    private Mono<ResponseEntity<String>> createEntityAndSaveToDatabase(PurchaseTicketRequest request, PayPallMakePaymentResponse response, Date currentDate, ObjectId consumerId) {
        try {
            PurchaseEntity purchaseEntity = purchaseFactory.createPurchase(request, currentDate, consumerId, response);

            save(purchaseEntity);
            return Mono.just(ResponseEntity.ok("Payment created, id: " + response.getId() + ", pay with PayPal account: " + getApprovalLink(response) +"\nTestowe konto do logowania: \ncv@example.pl\nPaC}X-D6"));
        } catch (Exception e) {
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during save to database: " + e.getMessage()));
        }

    }

    private String getApprovalLink(PayPallMakePaymentResponse response) {
        return response.getLinks().stream()
                .filter(link -> link.getRel().equals("approve"))
                .findFirst()
                .map(Link::getHref)
                .orElse(null);
    }

    public Mono<ResponseEntity<String>> acceptPayment(PurchaseAcceptRequest acceptRequest) {
        String bearerToken = tokenCacheService.getToken();
        if(bearerToken.isEmpty()){
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("PayPall server error, try again soon."));
        }

        return  payPallService.acceptPayment(bearerToken, acceptRequest.getToken())
                .flatMap(response -> {
                    String payPalId = response.getId();
                    try {
                        checkStatusPayPallByPayPalId(payPalId);
                        return Mono.just(ResponseEntity.ok("Payment accepted."));
                    } catch (RuntimeException e) {
                        return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                "Too late accept payment, error: " + e.getMessage()));
                    }
                });
    }
    private void checkStatusPayPallByPayPalId(String payPalId){
        try {
            PurchaseEntity purchaseEntity = purchaseRepository.findByPayPalId(payPalId);
            if (purchaseEntity.getStatusPay().equals("Created")) {
                updateStatusPayPalIdToPaid(purchaseEntity);
                return;
            }
            //wygasło więc sprawdzamy czy walidacja znowu się powiedzie
            EventEntity event = getEvent(purchaseEntity.getEventId());
            Date currentDate = new Date();
            payPalValidator.validate3Args(event, currentDate, purchaseEntity.getTickets());
            try{
                event.setTicketsBought(event.getTicketsBought() + purchaseEntity.getTickets());
                eventService.save(event);
            }catch (Exception e){
                throw new RuntimeException("Server error while blocking ticket quantity for 15 minutes: " + e.getMessage());
            }
            updateStatusPayPalIdToPaid(purchaseEntity);

        }catch(ValidationException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Too late accept payment, error: " + e.getMessage());
        }catch (Exception e){
            throw new RuntimeException("CheckStatus: " + e.getMessage());
        }
    }


    private void updateStatusPayPalIdToPaid(PurchaseEntity purchaseEntity){
        try {
            if(purchaseEntity.getStatusPay().equals("Created")) {
                purchaseEntity.setStatusPay("Paid");
                purchaseEntity.setExpiredDate(null);
                save(purchaseEntity);
                return;
            }
            purchaseEntity.setStatusPay("Paid");
            purchaseEntity.setExpiredDate(null);
            save(purchaseEntity);
        }catch (Exception e){
            throw new RuntimeException("Error while update status payPallId");
        }
    }

    public ResponseEntity<?> getPaymentById(String payPallId){
        try{
                PurchaseEntity purchaseEntity = purchaseRepository.findByPayPalId(payPallId);
            if (purchaseEntity != null) {
                PurchaseReponse purchaseResponse = purchaseMapper.toResponse(purchaseEntity);
                return new ResponseEntity<>(purchaseResponse, HttpStatus.OK);
            }

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found payment with id: " + payPallId);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while get payment by id");
        }
    }
}
