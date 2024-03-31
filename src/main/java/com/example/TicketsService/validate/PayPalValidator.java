package com.example.TicketsService.validate;

import com.example.TicketsService.model.EventEntity;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.util.Date;

@Component
public class PayPalValidator implements Validator {
    @Value("${TicketsService.app.delayedPaymentTimeMS}")
    private Long delayedPaymentTimeMS;
    @Override
    public void validate(Object object) throws ValidationException {
        if(!(object instanceof String bererToken)) {
            throw new IllegalArgumentException("Invalid object type");
        }
            if (bererToken.isEmpty()) {
                throw new ValidationException("PayPall server error, try again soon.");
            }
    }

    @Override
    public void validate3Args(Object arg1, Object arg2, Object arg3) throws ValidationException {
        if(!(arg1 instanceof EventEntity event)) {
            throw new IllegalArgumentException("Invalid object type");
        }
        if(!(arg2 instanceof Date currentDate)) {
            throw new IllegalArgumentException("Invalid object type");
        }
        if(!(arg3 instanceof Integer ticketsNumber)) {
            throw new IllegalArgumentException("Invalid object type");
        }

        Date eventDate = event.getDate();
        if (!checkDateIsBeforeEvent(eventDate, currentDate)) {
            throw new RuntimeException("It's too late, buy ticket to next event!");
        }
        if (event.getTicketsBought() + ticketsNumber > event.getTicketsNumber()) {
            throw new RuntimeException("We ran out of tickets, good luck next time");
        }
    }

    private boolean checkDateIsBeforeEvent(Date eventDate, Date currentDate) {
        Date eventDelayedDate = new Date(eventDate.getTime() + delayedPaymentTimeMS);
        return eventDelayedDate.compareTo(currentDate) > 0;

    }
}