package com.example.TicketsService.Mapper;

import com.example.TicketsService.Mapper.common.AbstractMapper;
import com.example.TicketsService.dto.response.PurchaseReponse;
import com.example.TicketsService.model.PurchaseEntity;
import org.springframework.stereotype.Component;
@Component
public class PurchaseMapper extends AbstractMapper<PurchaseEntity, PurchaseReponse> {

    @Override
    public PurchaseReponse toResponse(PurchaseEntity purchaseEntity){
        if(purchaseEntity == null)
            return null;

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

}