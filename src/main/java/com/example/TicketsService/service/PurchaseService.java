package com.example.TicketsService.service;

import com.example.TicketsService.model.PurchaseEntity;
import com.example.TicketsService.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    public PurchaseEntity save(PurchaseEntity purchase){return purchaseRepository.save(purchase); }

    public void updateStatusPayPalIdToPaid(String payPalId){
        try {
            PurchaseEntity purchaseEntity = purchaseRepository.findByPayPalId(payPalId);
            purchaseEntity.setStatusPay("Paid");
            purchaseRepository.save(purchaseEntity);
        }catch (Exception e){
            throw new RuntimeException("Error while update status payPallId");
        }
    }

}
