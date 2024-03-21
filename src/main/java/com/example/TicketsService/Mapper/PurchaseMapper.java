package com.example.TicketsService.Mapper;

import com.example.TicketsService.dto.response.EventResponse;
import com.example.TicketsService.dto.response.PurchaseReponse;
import com.example.TicketsService.model.ArtistEntity;
import com.example.TicketsService.model.EventEntity;
import com.example.TicketsService.model.PurchaseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PurchaseMapper {

    public PurchaseReponse toResponse(PurchaseEntity purchaseEntity){
        PurchaseReponse purchaseResponse = new PurchaseReponse();

        purchaseResponse.setId(purchaseEntity.getId().toString());
        purchaseResponse.setEventId(purchaseEntity.getEventId());
        purchaseResponse.setDate(purchaseEntity.getDate());
        purchaseResponse.setConsumerId(purchaseEntity.getConsumerId());
        purchaseResponse.setTickets(purchaseEntity.getTickets());
        purchaseResponse.setPayPalId(purchaseEntity.getPayPalId());
        purchaseResponse.setStatusPay(purchaseEntity.getStatusPay());

        return purchaseResponse;
    }

    public List<PurchaseReponse> toResponses(List<PurchaseEntity> purchaseEntityList){
        List<PurchaseReponse> purchaseResponses = new ArrayList<>();

        for(PurchaseEntity purchaseEntity : purchaseEntityList){
            purchaseResponses.add(toResponse(purchaseEntity));
        }
        return purchaseResponses;
    }
}