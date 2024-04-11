package com.example.TicketsService.controller;


import com.example.TicketsService.dto.request.CreateEventRequest;
import com.example.TicketsService.dto.response.EventResponse;
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

    final
    EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Operation(summary = "Dodaj wydarzenia", description = "Pozwala utworzyć jedno lub więcej nowych wydarzeń na podstawie przekazanych danych wejściowych. " +
    "Endpoint dostępny dla zalogowanych użytkowników, z rolą 'MANAGER'")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<?> createEvents(@Valid @RequestBody CreateEventRequest request) {
        return eventService.createEvents(request);
    }


    @Operation(summary = "Usuń wydarzenie", description = "Pozwala usunąć własne wydarzenie o podanym id",parameters = {
            @Parameter(name = "eventId", description = "Id wydarzenia", required = true, example = "65f3a110f4c72238e8e32579")
    })
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<?> deleteEvent(@PathVariable String eventId) {
        return eventService.deleteEvent(eventId);
    }

    @Operation(summary = "Pobierz wszystkie wydarzenia", description = "Pobiera wszystkie wydarzenia")
    @GetMapping("/all")
    public ResponseEntity<List<EventResponse>> getAllEvent(){
        List<EventResponse> events = eventService.getAllEventsResponse();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @Operation(summary = "Pobierz wydarzenie o określonym id", description = "Pobiera wydarzenie o podanym id", parameters = {
        @Parameter(name = "eventId", description = "Id wydarzenia", required = true, example = "65b590e4aa11c12eb2294ba8")
    })
    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable String eventId) {
            EventResponse event = eventService.getEventById(eventId);
            return ResponseEntity.ok(event);
    }
}