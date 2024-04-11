package com.example.TicketsService.Mapper;

import com.example.TicketsService.Factory.EventFactory;
import com.example.TicketsService.Mapper.common.AbstractMapper;
import com.example.TicketsService.dto.request.CreateEventRequest;
import com.example.TicketsService.dto.response.EventResponse;
import com.example.TicketsService.model.EventEntity;
import com.example.TicketsService.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventMapper extends AbstractMapper<EventEntity, EventResponse> {

    private final EventFactory eventFactory;

    @Autowired
    public EventMapper(EventFactory eventFactory){
        this.eventFactory = eventFactory;
    }

    public List<EventEntity> mapToEvents(CreateEventRequest request, UserDetailsImpl userDetails) {
        return request.getDates().stream()
                .map(date -> eventFactory.createEvent(date, request, userDetails))
                .collect(Collectors.toList());
    }

    @Override
    public EventResponse toResponse(EventEntity eventEntity){
        EventResponse eventResponse = new EventResponse();

        eventResponse.setId(eventEntity.getId().toString());
        eventResponse.setDate(eventEntity.getDate());
        eventResponse.setArtistName(eventEntity.getArtistName());
        eventResponse.setPrice(eventEntity.getPrice());
        eventResponse.setTicketsNumber(eventEntity.getTicketsNumber());
        eventResponse.setTicketsBought(eventEntity.getTicketsBought());
        eventResponse.setLocation(eventEntity.getLocation());
        eventResponse.setCity(eventEntity.getCity());
        eventResponse.setPostcode(eventEntity.getPostcode());
        eventResponse.setRegon(eventEntity.getRegon());
        eventResponse.setStreet(eventEntity.getStreet());
        eventResponse.setArtistId(eventEntity.getArtistId());
        eventResponse.setName(eventEntity.getName());
        eventResponse.setDescription(eventEntity.getDescription());

        return eventResponse;
    }
}
