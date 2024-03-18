package com.example.TicketsService.service;

import com.example.TicketsService.dto.request.CreateEventRequest;
import com.example.TicketsService.dto.response.MessageResponse;
import com.example.TicketsService.model.EventEntity;
import com.example.TicketsService.repository.EventRepository;
import com.example.TicketsService.security.service.UserDetailsImpl;
import jakarta.validation.ValidationException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Transactional
    public ResponseEntity<?> createEvents(CreateEventRequest request) {
        try {
            validateData(request);
            List<EventEntity> events = mapToEvents(request);
            saveAll(events);
            return ResponseEntity.ok(new MessageResponse("Created event successfully"));
        }catch(ValidationException e){
            return ResponseEntity.badRequest().body(new MessageResponse("Validation error: " + e.getMessage()));
        }
        catch (DataAccessException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Database error: " + e.getMessage()));
        }
    }

    private void validateData(CreateEventRequest request) throws ValidationException {
        if (!request.isValidRegon()) {
            throw new ValidationException("Error, region not in enum list");
        }
        if (request.getDates().isEmpty()) {
            throw new ValidationException("Error, date is empty");
        }
    }

    private List<EventEntity> mapToEvents(CreateEventRequest request) {
        return request.getDates().stream().map(date -> mapToEvent(date, request)).collect(Collectors.toList());
    }

    private EventEntity mapToEvent(Date date, CreateEventRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new EventEntity(
                date,
                request.getArtistName(),
                request.getPrice(),
                request.getTicketsNumber(),
                0,
                request.getLocation(),
                request.getCity(),
                request.getPostcode(),
                request.getRegon(),
                request.getStreet(),
                userDetails.getId().toHexString(),
                request.getName(),
                request.getDescription()
        );
    }


    private void saveAll(List<EventEntity> events) throws DataAccessException {
        eventRepository.saveAll(events);
    }
    public void save(EventEntity event) {
        eventRepository.save(event);
    }

    public List<EventEntity> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<EventEntity> getEventEntityByEventId(ObjectId id) {
        return eventRepository.findById(id);
    }

    public ResponseEntity<?> deleteEvent(String eventId) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            ObjectId userId = userDetails.getId();
            if (checkCanDeleteEvent(userId, eventId)) {
                eventRepository.deleteById(new ObjectId(eventId));
                return ResponseEntity.ok(new MessageResponse("Deleted event successfully"));
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse("You can't delete this event"));
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new MessageResponse("Database error while deleting event: " + e.getMessage()));
        }
    }

    private boolean checkCanDeleteEvent(ObjectId userId, String eventId) {
        return eventRepository.existsByCreatedByAndId(eventId, userId.toHexString());
    }

    public EventEntity getEventById(String eventId) {
        return eventRepository.findById(new ObjectId(eventId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found with id: " + eventId));

    }
}
