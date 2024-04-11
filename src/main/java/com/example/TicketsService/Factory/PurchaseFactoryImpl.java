package com.example.TicketsService.Factory;

import com.example.TicketsService.dto.request.PurchaseTicketRequest;
import com.example.TicketsService.dto.response.PayPallMakePaymentResponse;
import com.example.TicketsService.model.PurchaseEntity;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class PurchaseFactoryImpl implements PurchaseFactory {

    @Value("${TicketService.app.timeLockTicketsMS}")
    private int ticketsLock;

    @Override
    public PurchaseEntity createPurchase(PurchaseTicketRequest request, Date currentDate, ObjectId consumerId, PayPallMakePaymentResponse response){
        return new PurchaseEntity(request.getEventId(), currentDate, consumerId, request.getTickets(), response.getId(), "Created", new Date(currentDate.getTime() + ticketsLock));
    }

}
