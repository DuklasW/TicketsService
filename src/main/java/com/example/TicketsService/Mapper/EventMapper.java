package com.example.TicketsService.Mapper;

import com.example.TicketsService.dto.request.CreateEventRequest;
import com.example.TicketsService.model.EventEntity;
import com.example.TicketsService.security.service.UserDetailsImpl;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventMapper {
    public EventEntity mapToEvent(Date date, CreateEventRequest request, UserDetailsImpl userDetails) {
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

    public List<EventEntity> mapToEvents(CreateEventRequest request, UserDetailsImpl userDetails) {
        return request.getDates().stream()
                .map(date -> mapToEvent(date, request, userDetails))
                .collect(Collectors.toList());
    }
}
