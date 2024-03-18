package com.example.TicketsService.service;

import com.example.TicketsService.model.ConsumerEntity;
import com.example.TicketsService.model.UserEntity;
import com.example.TicketsService.repository.ConsumerRepository;
import com.example.TicketsService.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class ConsumerService {

    @Autowired
    private ConsumerRepository consumerRepository;
    @Autowired
    private UserRepository userRepository;

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

}
