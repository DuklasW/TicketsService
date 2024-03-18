package com.example.TicketsService.controller;

import com.example.TicketsService.dto.request.PurchaseAcceptRequest;
import com.example.TicketsService.dto.request.PurchaseTicketRequest;
import com.example.TicketsService.dto.response.Link;
import com.example.TicketsService.dto.response.MessageResponse;
import com.example.TicketsService.dto.response.PayPallAcceptPaymentReponse;
import com.example.TicketsService.dto.response.PayPallMakePaymentResponse;
import com.example.TicketsService.model.EventEntity;
import com.example.TicketsService.model.PurchaseEntity;
import com.example.TicketsService.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Optional;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private PayPallService payPallService;

    @Autowired
    private EventService eventService;

    @Autowired
    private TokenCacheService tokenCacheService;

    @Autowired
    private JsonService jsonService;

    @Value("${TicketsService.app.delayedPaymentTimeMS}")
    private Long delayedPaymentTimeMS;


    //Funkcja która tworzy płatność, zwraca linka do strony paypall na którą należy się zalogować
    @PostMapping("/makePayment2")
    public Mono<ResponseEntity<String>> makePayment2(@Valid @RequestBody PurchaseTicketRequest request, HttpServletRequest tokenRequest){

        String userEmail = tokenService.getEmailFromHttpServletRequest(tokenRequest);
        ObjectId consumerId = consumerService.getConsumerIdByEmail(userEmail);
        String jsonBodyPayment;

        EventEntity event = eventService.getEventEntityByEventId(new ObjectId(request.getEventId())).orElseThrow(() -> new RuntimeException("Brak wydarzenia o podanym id!"));

        Date eventDate = event.getDate();
        Date currentDate = new Date();

        if(checkDateIsBeforeEvent(eventDate, currentDate)){
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("It's too late, buy ticket to next event!"));
        }

        if(event.getTicketsBought() + request.getTickets() > event.getTicketsNumber()){
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("We ran out of tickets, good luck next time"));
        }

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
        if(bererToken.isEmpty()){
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("PayPall server error, try again soon."));
        }


            return payPallService.makePayment(bererToken, jsonBodyPayment)
                    .flatMap(response -> createEntityAndSaveToDatabase(request, response, currentDate, consumerId))
                    .onErrorResume(error -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment failed: " + error.getMessage())))
                    .defaultIfEmpty(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error with payment!"));

    }

    private Mono<ResponseEntity<String>> createEntityAndSaveToDatabase(PurchaseTicketRequest request, PayPallMakePaymentResponse response,  Date currentDate, ObjectId consumerId){
        PurchaseEntity purchaseEntity = new PurchaseEntity(
                new ObjectId(request.getEventId()),
                currentDate,
                consumerId,
                request.getTickets(),
                response.getId(),
                "Created");

        if(purchaseService.save(purchaseEntity) != null){
            return  Mono.just(ResponseEntity.ok("Payment created, id: " + response.getId() + ", pay with PayPal account: " + getApprovalLink(response) +"\nTestowe konto do logowania: \nsb-u4l64329043544@personal.example.com\nPaC}X-D6"));
        }else{
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during save to database"));
        }
    }

    //Sprawdza czy wydarzenie już się nie rozpoczeło, pozwala kupić bilet nawet po zaczęciu jeśli nie przekracza określonego czasu: delayedPaymentTimeMS
    private boolean checkDateIsBeforeEvent(Date eventDate, Date currentDate){
        Date eventDelayedDate = new Date(eventDate.getTime() + delayedPaymentTimeMS);
        return eventDelayedDate.compareTo(currentDate) > 0;
    }

    //Sortuje odpowiedź z Api paypall i zwraca tylko jeden link służący do potwierdzenia płatności poprzez zalogowanie się
    private String getApprovalLink(PayPallMakePaymentResponse response){
        return response.getLinks().stream()
                .filter(link -> link.getRel().equals("approve"))
                .findFirst()
                .map(Link::getHref)
                .orElse(null);
    }

    //EndPoint na który przychodzi potwierdzenie płatności
    @PostMapping("acceptPayment")
    public Mono<ResponseEntity<String>> acceptPayment(@Valid @RequestBody PurchaseAcceptRequest acceptRequest){

        String bearerToken = tokenCacheService.getToken();
        if(bearerToken.isEmpty()){
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("PayPall server error, try again soon."));
        }

        return  payPallService.acceptPayment(bearerToken, acceptRequest.getToken())
                .flatMap(response -> {
                    String payPalId = response.getId();
                    try {
                        purchaseService.updateStatusPayPalIdToPaid(payPalId);
                        return Mono.just(ResponseEntity.ok("Payment accepted."));
                    } catch (RuntimeException e) {
                        return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                "Too late accept payment, error: " + e.getMessage()));
                    }
                });
    }


    //Funkcja testowa do zdobycia berer tokena od API paypall
    @GetMapping("/testGetBerrerToken")
    public Mono<ResponseEntity<String>> getBererToken(){
        return payPallService.getAccessToken()
                .map(tokenResponse -> ResponseEntity.ok(tokenResponse.getAccess_token()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
