package com.example.TicketsService.service;

import com.example.TicketsService.Mapper.PurchaseMapper;
import com.example.TicketsService.dto.response.PurchaseReponse;
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
    private final PurchaseMapper purchaseMapper;

    @Autowired
    public ConsumerService(ConsumerRepository consumerRepository, UserRepository userRepository, PurchaseRepository purchaseRepository, PurchaseMapper purchaseMapper) {
        this.consumerRepository = consumerRepository;
        this.userRepository = userRepository;
        this.purchaseRepository = purchaseRepository;
        this.purchaseMapper = purchaseMapper;
    }

    public ConsumerEntity save(ConsumerEntity consumer) {return consumerRepository.save(consumer); }

    public ObjectId getConsumerIdByEmail(String email){
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        ConsumerEntity consumer = consumerRepository.findConsumerEntityByUserId(user.getId());
        if(consumer == null){
            throw new RuntimeException("Consumer entity not found!");
        }else {
            return consumer.getId();
        }
    }

    public List<PurchaseReponse> getTicketHistory(String userEmail) {
        ObjectId consumerId = getConsumerIdByEmail(userEmail);
        List<PurchaseEntity> purchases = purchaseRepository.findByConsumerId(consumerId);
        return purchaseMapper.toResponses(purchases);
    }

    public ConsumerEntity getConsumerByUserId(ObjectId userId) {
        return consumerRepository.findConsumerEntityByUserId(userId);
    }
}
