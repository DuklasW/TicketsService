package com.example.TicketsService.validate;

import jakarta.validation.ValidationException;

public interface Validator {

    void validate(Object object) throws ValidationException;

    default void validate3Args(Object arg1, Object arg2, Object arg3) throws ValidationException {
        throw new UnsupportedOperationException("This method is not supported by this validator.");
    }
}
