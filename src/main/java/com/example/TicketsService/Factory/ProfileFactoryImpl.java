package com.example.TicketsService.Factory;

import com.example.TicketsService.dto.request.SignUpConsumerRequest;
import com.example.TicketsService.dto.request.SignUpManagerRequest;
import com.example.TicketsService.model.ConsumerEntity;
import com.example.TicketsService.model.ManagerEntity;
import org.springframework.stereotype.Component;

@Component
public class ProfileFactoryImpl implements ProfileFactory {


    @Override
    public ConsumerEntity createConsumer(String userId, SignUpConsumerRequest signUpConsumerRequest) {
        return new ConsumerEntity(userId, signUpConsumerRequest.getName(), signUpConsumerRequest.getSurname(), signUpConsumerRequest.getCity(), signUpConsumerRequest.getPhone(), signUpConsumerRequest.getPostcode(), signUpConsumerRequest.getRegon(), signUpConsumerRequest.getStreet());
    }

    @Override
    public ManagerEntity createManager(String userId, SignUpManagerRequest signUpManagerRequest) {
        return new ManagerEntity(userId, signUpManagerRequest.getName(), signUpManagerRequest.getCheckVat(), signUpManagerRequest.getCity(), signUpManagerRequest.getCompanyName(), signUpManagerRequest.getCompanyStreet(), signUpManagerRequest.getNip(), signUpManagerRequest.getPhone(), signUpManagerRequest.getPostcode(), signUpManagerRequest.getRegon());

    }
}
