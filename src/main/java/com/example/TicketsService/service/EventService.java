package com.example.TicketsService.service;

import com.example.TicketsService.model.EventEntity;
import com.example.TicketsService.repository.EventRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public EventEntity save(EventEntity event){return eventRepository.save(event); }

    public List<EventEntity> getAllEvents(){
        return eventRepository.findAll();
    }

    public Optional<EventEntity> getEventEntityByEventId(ObjectId id){return eventRepository.findById(id); }
}
