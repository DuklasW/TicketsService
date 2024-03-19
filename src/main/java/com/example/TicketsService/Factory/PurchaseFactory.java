package com.example.TicketsService.Factory;

import com.example.TicketsService.dto.request.PurchaseTicketRequest;
import com.example.TicketsService.dto.response.PayPallMakePaymentResponse;
import com.example.TicketsService.model.PurchaseEntity;
import org.bson.types.ObjectId;

import java.util.Date;

public interface PurchaseFactory {

    PurchaseEntity createPurchase(PurchaseTicketRequest request, Date currentDate, ObjectId consumerId, PayPallMakePaymentResponse response);
}
