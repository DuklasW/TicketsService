package com.example.TicketsService.service;

import com.example.TicketsService.dto.request.PurchaseAcceptRequest;
import com.example.TicketsService.dto.request.PurchaseTicketRequest;
import com.example.TicketsService.dto.response.Link;
import com.example.TicketsService.dto.response.PayPallMakePaymentResponse;
import com.example.TicketsService.model.PurchaseEntity;
import com.example.TicketsService.repository.PurchaseRepository;
import com.example.TicketsService.security.service.UserDetailsImpl;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import com.example.TicketsService.model.EventEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class PurchaseService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private EventService eventService;

    @Autowired
    private JsonService jsonService;

    @Autowired
    private PayPallService payPallService;

    @Autowired
    private TokenCacheService tokenCacheService;

    @Value("${TicketsService.app.delayedPaymentTimeMS}")
    private Long delayedPaymentTimeMS;

    @Autowired
    private PurchaseRepository purchaseRepository;

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

        validateEventForPurchase(event, currentDate, request.getTickets());

        try{
            event.setTicketsBought(event.getTicketsBought() + request.getTickets());
            eventService.save(event);
        }catch (Exception e){
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error while blocking ticket quantity for 15 minutes"));
        }

        try{
            jsonBodyPayment = jsonService.createPaymentDataJson(event, request.getTickets());
        }catch (Exception e){
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Server error, try again soon."));
        }

        String bererToken = tokenCacheService.getToken();
        validatePaymentToken(bererToken);


            return payPallService.makePayment(bererToken, jsonBodyPayment)
                    .flatMap(response -> createEntityAndSaveToDatabase(request, response, currentDate, consumerId))
                    .onErrorResume(error -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment failed: " + error.getMessage())))
                    .defaultIfEmpty(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error with payment!"));
    }
    private EventEntity getEvent(String eventId) {
        return eventService.getEventEntityByEventId(new ObjectId(eventId))
                .orElseThrow(() -> new RuntimeException("Brak wydarzenia o podanym id!"));
    }
    private ObjectId getConsumerId(String userEmail) {
        return consumerService.getConsumerIdByEmail(userEmail);
    }
    private void validatePaymentToken(String bererToken) {
        if (bererToken.isEmpty()) {
            throw new RuntimeException("PayPall server error, try again soon.");
        }
    }

    private void validateEventForPurchase(EventEntity event, Date currentDate, int ticketsNumber) {
        Date eventDate = event.getDate();
        if (!checkDateIsBeforeEvent(eventDate, currentDate)) {
            throw new RuntimeException("It's too late, buy ticket to next event!");
        }
        if (event.getTicketsBought() + ticketsNumber > event.getTicketsNumber()) {
            throw new RuntimeException("We ran out of tickets, good luck next time");
        }
    }

    private Mono<ResponseEntity<String>> createEntityAndSaveToDatabase(PurchaseTicketRequest request, PayPallMakePaymentResponse response, Date currentDate, ObjectId consumerId) {
        try {
            PurchaseEntity purchaseEntity = new PurchaseEntity(
                    request.getEventId(),
                    currentDate,
                    consumerId,
                    request.getTickets(),
                    response.getId(),
                    "Created");

            save(purchaseEntity);
            return Mono.just(ResponseEntity.ok("Payment created, id: " + response.getId() + ", pay with PayPal account: " + getApprovalLink(response) +"\nTestowe konto do logowania: \nsb-u4l64329043544@personal.example.com\nPaC}X-D6"));
        } catch (Exception e) {
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during save to database: " + e.getMessage()));
        }

    }

    private boolean checkDateIsBeforeEvent(Date eventDate, Date currentDate) {
        Date eventDelayedDate = new Date(eventDate.getTime() + delayedPaymentTimeMS);
        return eventDelayedDate.compareTo(currentDate) > 0;

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
                        updateStatusPayPalIdToPaid(payPalId);
                        return Mono.just(ResponseEntity.ok("Payment accepted."));
                    } catch (RuntimeException e) {
                        return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                "Too late accept payment, error: " + e.getMessage()));
                    }
                });
    }

    private void updateStatusPayPalIdToPaid(String payPalId){
        try {
            PurchaseEntity purchaseEntity = purchaseRepository.findByPayPalId(payPalId);
            purchaseEntity.setStatusPay("Paid");
            purchaseRepository.save(purchaseEntity);
        }catch (Exception e){
            throw new RuntimeException("Error while update status payPallId");
        }
    }
}
