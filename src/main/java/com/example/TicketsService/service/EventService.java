package com.example.TicketsService.service;

import com.example.TicketsService.Mapper.EventMapper;
import com.example.TicketsService.dto.request.CreateEventRequest;
import com.example.TicketsService.dto.response.EventResponse;
import com.example.TicketsService.dto.response.MessageResponse;
import com.example.TicketsService.model.EventEntity;
import com.example.TicketsService.repository.EventRepository;
import com.example.TicketsService.security.service.UserDetailsImpl;
import com.example.TicketsService.validate.EventValidator;
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
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventValidator eventValidator;
    private final EventMapper eventMapper;

    @Autowired
    public EventService(EventRepository eventRepository, EventValidator eventValidator, EventMapper eventMapper){
        this.eventRepository = eventRepository;
        this.eventValidator = eventValidator;
        this.eventMapper = eventMapper;
    }

    @Transactional
    public ResponseEntity<?> createEvents(CreateEventRequest request) {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            eventValidator.validate(request);
            List<EventEntity> events = eventMapper.mapToEvents(request, userDetails);
            saveAll(events);
            return ResponseEntity.ok(new MessageResponse("Created event successfully"));
        }catch(ValidationException e){
            return ResponseEntity.badRequest().body(new MessageResponse("Validation error: " + e.getMessage()));
        }
        catch (DataAccessException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Database error: " + e.getMessage()));
        }
    }



    private void saveAll(List<EventEntity> events) throws DataAccessException {
        eventRepository.saveAll(events);
    }
    public void save(EventEntity event) {
        eventRepository.save(event);
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

    public EventResponse getEventById(String eventId) {
        EventEntity eventEntity = eventRepository.findById(new ObjectId(eventId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found with id: " + eventId));
        return eventMapper.toResponse(eventEntity);
    }

    public List<EventResponse> getAllEventsResponse() {
        List<EventEntity> events = eventRepository.findAll();
        return eventMapper.toListResponse(events);
    }
}
