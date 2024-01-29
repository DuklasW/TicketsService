package com.example.TicketsService.service;

import com.example.TicketsService.model.PurchaseEntity;
import com.example.TicketsService.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    public PurchaseEntity save(PurchaseEntity purchase){return purchaseRepository.save(purchase); }
}
