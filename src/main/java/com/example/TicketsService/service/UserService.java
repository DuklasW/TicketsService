package com.example.TicketsService.service;


import com.example.TicketsService.model.UserEntity;
import com.example.TicketsService.repository.UserRepository;
import com.example.TicketsService.security.jwt.JwtUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public List<UserEntity> getAllUsers(){ return userRepository.findAll(); }

    public UserEntity save(UserEntity user){
        return userRepository.save(user);
    }

    public Optional<UserEntity> getUserByUserId(ObjectId id) {
        return userRepository.findById(id);
    }

    public Optional<UserEntity> getUserByUserEmail(String email) { return userRepository.findByEmail(email); }


    public boolean checkUserExistByEmail(String email) { return  userRepository.existsByEmail(email); }

    public void deleteByUserId(ObjectId userId){
        userRepository.deleteById(userId);
    }


}
