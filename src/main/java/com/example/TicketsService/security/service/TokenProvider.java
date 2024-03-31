package com.example.TicketsService.security.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TokenProvider {
    public String generateToken() {
        return UUID.randomUUID().toString();
    }
}
