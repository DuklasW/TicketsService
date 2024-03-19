package com.example.TicketsService.validate;

import com.example.TicketsService.dto.request.CreateEventRequest;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class EventValidator implements Validator {

    @Override
    public void validate(Object object) throws ValidationException {
        if(!(object instanceof CreateEventRequest createEventRequest)) {
            throw new IllegalArgumentException("Invalid object type");
        }

        if (!createEventRequest.isValidRegon()) {
            throw new ValidationException("Error, region not in enum list");
        }

        if (createEventRequest.getDates().isEmpty()) {
            throw new ValidationException("Error, date is empty");
        }
    }
}
