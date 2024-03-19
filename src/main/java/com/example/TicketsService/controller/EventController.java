package com.example.TicketsService.controller;


import com.example.TicketsService.dto.request.CreateEventRequest;
import com.example.TicketsService.model.EventEntity;
import com.example.TicketsService.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name="Event Controller", description = "Kontroler służący do zadządzania wydarzeniami")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    EventService eventService;

    @Operation(summary = "Dodaj wydarzenia", description = "Pozwala utworzyć jedno lub więcej nowych wydarzeń na podstawie przekazanych danych wejściowych")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createEvents(@Valid @RequestBody CreateEventRequest request) {
        return eventService.createEvents(request);
    }


    @Operation(summary = "Usuń wydarzenie", description = "Pozwala usunąć wydarzenie o podanym id",parameters = {
            @Parameter(name = "eventId", description = "Id wydarzenia", required = true, example = "65f3a110f4c72238e8e32579")
    })
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<?> deleteEvent(@PathVariable String eventId) {
        return eventService.deleteEvent(eventId);
    }

    @Operation(summary = "Pobierz wszystkie wydarzenia", description = "Pobiera wszystkie wydarzenia")
    @GetMapping("/all")
    public ResponseEntity<List<EventEntity>> getAllEvent(){
        List<EventEntity> events = eventService.getAllEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @Operation(summary = "Pobierz wydarzenie o określonym id", description = "Pobiera wydarzenie o podanym id", parameters = {
        @Parameter(name = "eventId", description = "Id wydarzenia", required = true, example = "65f3a110f4c72238e8e32579")
    })
    @GetMapping("/{eventId}")
    public ResponseEntity<EventEntity> getEventById(@PathVariable String eventId) {
            EventEntity event = eventService.getEventById(eventId);
            return ResponseEntity.ok(event);
    }
}