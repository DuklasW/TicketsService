package com.example.TicketsService.Factory;

import com.example.TicketsService.dto.request.SignUpConsumerRequest;
import com.example.TicketsService.dto.request.SignUpManagerRequest;
import com.example.TicketsService.model.ConsumerEntity;
import com.example.TicketsService.model.ManagerEntity;

public interface ProfileFactory {
    ConsumerEntity createConsumer(String userId, SignUpConsumerRequest consumerRequest);

    ManagerEntity createManager(String userId, SignUpManagerRequest managerRequest);
}
