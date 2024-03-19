package com.example.TicketsService.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtValidationException extends AuthenticationException {

    public JwtValidationException(String msg) {
        super(msg);
    }

}
