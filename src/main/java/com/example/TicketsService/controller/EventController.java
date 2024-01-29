package com.example.TicketsService.controller;

import com.example.TicketsService.dto.request.CreateEventRequest;
import com.example.TicketsService.dto.response.MessageResponse;
import com.example.TicketsService.model.EventEntity;
import com.example.TicketsService.service.EventService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Validated
@Valid
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    EventService eventService;


    @PostMapping("create")
    public ResponseEntity<?> createEvents(@Valid @RequestBody CreateEventRequest request) {

        if (!request.isValidRegon()){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error, region not in enum list"));
        }

        if(request.getDates().isEmpty()){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error, date is empty"));
        }

        List<Date> dates = request.getDates();
        dates.stream().map(date -> {
            EventEntity event = new EventEntity(
                    date,
                    request.getArtistName(),
                    request.getPrice(),
                    request.getNumberTickets(),
                    request.getLocation(),
                    request.getCity(),
                    request.getPostcode(),
                    request.getRegon(),
                    request.getStreet(),
                    request.getCreatedByByObjectId(),
                    request.getName(),
                    request.getDescription()
            );
            eventService.save(event);
            return null;
        }).collect(Collectors.toSet());


        return ResponseEntity.ok(new MessageResponse("Added events successfully"));
    }

    @GetMapping("/all")
    public ResponseEntity<List<EventEntity>> getAllEvent(){
        List<EventEntity> events = eventService.getAllEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }
}