package com.example.TicketsService.service;


import com.example.TicketsService.Mapper.UserMapper;
import com.example.TicketsService.dto.response.UserResponse;
import com.example.TicketsService.model.UserEntity;
import com.example.TicketsService.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }


    public List<UserResponse> getAllUsers(){
        List<UserEntity> users = userRepository.findAll();
        return userMapper.toResponses(users);
    }

    public UserEntity save(UserEntity user){
        return userRepository.save(user);
    }

    public UserResponse getUserByUserId(ObjectId id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toResponse(userEntity);
    }

    public Optional<UserEntity> getUserByUserEmail(String email) { return userRepository.findByEmail(email); }

    public boolean checkUserExistByEmail(String email) { return  userRepository.existsByEmail(email); }

    public void deleteByUserId(ObjectId userId){
        userRepository.deleteById(userId);
    }


}
