package com.example.TicketsService.Factory;

import com.example.TicketsService.dto.request.CreateEventRequest;
import com.example.TicketsService.model.EventEntity;
import com.example.TicketsService.security.service.UserDetailsImpl;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class EventFactoryImpl implements EventFactory {
    @Override
    public EventEntity createEvent(Date date, CreateEventRequest request, UserDetailsImpl userDetails) {
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
}
