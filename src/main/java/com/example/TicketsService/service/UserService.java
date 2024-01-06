package com.example.TicketsService.service;


import com.example.TicketsService.model.UserEntity;
import com.example.TicketsService.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserEntity> allUsers(){
        return userRepository.findAll();
    }

    //ponieważ może nie zwrócić nic
    public Optional<UserEntity> singleUser(ObjectId id) {
        return userRepository.findById(id);
    }
}
