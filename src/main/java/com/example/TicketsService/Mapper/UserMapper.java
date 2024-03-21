package com.example.TicketsService.Mapper;
import com.example.TicketsService.Mapper.common.AbstractMapper;

import com.example.TicketsService.dto.response.UserResponse;
import com.example.TicketsService.model.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper extends AbstractMapper<UserEntity, UserResponse>{
    @Override
    public UserResponse toResponse(UserEntity entity) {
        if(entity == null)
            return null;
        UserResponse userResponse = new UserResponse();

        userResponse.setId(entity.getId().toString());
        userResponse.setEmail(entity.getEmail());
        userResponse.setPassword(entity.getPassword());
        userResponse.setRoles(List.of(entity.getRoles().toString()));

        return userResponse;
    }
}
