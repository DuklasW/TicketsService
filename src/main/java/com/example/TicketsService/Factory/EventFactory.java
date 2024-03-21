package com.example.TicketsService.Factory;

import com.example.TicketsService.dto.request.CreateEventRequest;
import com.example.TicketsService.model.EventEntity;
import com.example.TicketsService.security.service.UserDetailsImpl;

import java.util.Date;

public interface EventFactory {

    EventEntity createEvent(Date date, CreateEventRequest request, UserDetailsImpl userDetails);
}
