package com.example.TicketsService.service;

import com.example.TicketsService.model.ConsumerEntity;
import com.example.TicketsService.model.PurchaseEntity;
import com.example.TicketsService.model.UserEntity;
import com.example.TicketsService.repository.ConsumerRepository;
import com.example.TicketsService.repository.PurchaseRepository;
import com.example.TicketsService.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ConsumerService {

    private final ConsumerRepository consumerRepository;
    private final UserRepository userRepository;
    private final PurchaseRepository purchaseRepository;

    @Autowired
    public ConsumerService(ConsumerRepository consumerRepository, UserRepository userRepository, PurchaseRepository purchaseRepository) {
        this.consumerRepository = consumerRepository;
        this.userRepository = userRepository;
        this.purchaseRepository = purchaseRepository;
    }

    public ConsumerEntity save(ConsumerEntity consumer) {return consumerRepository.save(consumer); }

    public ObjectId getConsumerIdByEmail(String email){
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        ConsumerEntity consumer = consumerRepository.findConsumerEntityByUserId(user.getIdAsObjectId());
        if(consumer == null){
            throw new RuntimeException("Consumer entity not found!");
        }else {
            return consumer.getId();
        }
    }

    public List<PurchaseEntity> getTicketHistory(String userEmail) {
        ObjectId consumerId = getConsumerIdByEmail(userEmail);
        return purchaseRepository.findByConsumerId(consumerId);
    }

    public ConsumerEntity getConsumerByUserId(ObjectId userId) {
        return consumerRepository.findConsumerEntityByUserId(userId);
    }
}
