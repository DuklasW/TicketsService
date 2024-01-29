package com.example.TicketsService.controller;

import com.example.TicketsService.dto.request.PurchaseTicketRequest;
import com.example.TicketsService.dto.response.MessageResponse;
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

import java.util.Date;
import java.util.Optional;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Validated
@RequestMapping("/api/purchase")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ConsumerService consumerService;

    @Autowired
    private EventService eventService;

    @Value("${TicketsService.app.delayedPaymentTimeMS}")
    private Long delayedPaymentTimeMS;

    @PostMapping("/ticket")
    public ResponseEntity<?> buyTicket(@Valid @RequestBody PurchaseTicketRequest request, HttpServletRequest tokenRequest){

        String userEmail = tokenService.getEmailFromHttpServletRequest(tokenRequest);
        ObjectId consumerId = consumerService.getConsumerIdByEmail(userEmail);

        Optional<EventEntity> event = eventService.getEventEntityByEventId(new ObjectId(request.getEventId()));
        if(!event.isPresent()){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("No event with the given id was found!"));
        }

        Date eventDate = event.get().getDate();
        Date eventDelayedDate = new Date(eventDate.getTime() + delayedPaymentTimeMS);

        Date currentDate = new Date();

        if(eventDelayedDate.compareTo(currentDate) > 0){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("It's too late, buy ticket to next event!"));
        }

        PurchaseEntity purchaseEntity = new PurchaseEntity(
                new ObjectId(request.getEventId()),
                currentDate,
                consumerId,
                request.getTickets(),
                "Paid");

        if(purchaseService.save(purchaseEntity) != null){
            return ResponseEntity.ok(new MessageResponse("Paid successfully!"));
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error during payment"));
        }
    }

}
