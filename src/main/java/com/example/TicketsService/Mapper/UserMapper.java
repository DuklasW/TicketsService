package com.example.TicketsService.Mapper;

import com.example.TicketsService.dto.response.PurchaseReponse;
import com.example.TicketsService.dto.response.UserResponse;
import com.example.TicketsService.model.PurchaseEntity;
import com.example.TicketsService.model.UserEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {
    public UserResponse toResponse(UserEntity userEntity) {
        UserResponse userResponse = new UserResponse();

        userResponse.setId(userEntity.getId().toString());
        userResponse.setEmail(userEntity.getEmail());
        userResponse.setPassword(userEntity.getPassword());
        userResponse.setRoles(List.of(userEntity.getRoles().toString()));

        return userResponse;
    }


    public List<UserResponse> toResponses(List<UserEntity> users) {
        List<UserResponse> userResponses = new ArrayList<>();

        for(UserEntity user : users) {
            userResponses.add(toResponse(user));
        }
        return userResponses;
    }
}