package com.example.TicketsService.controller;

import com.example.TicketsService.model.UserEntity;
import com.example.TicketsService.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;
    @GetMapping
    public ResponseEntity<List<UserEntity>> getAllUsers(){
        return new ResponseEntity<List<UserEntity>>(userService.allUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<Optional<UserEntity>> getSingleUser(@PathVariable ObjectId id){
        return new ResponseEntity<Optional<UserEntity>>(userService.singleUser(id), HttpStatus.OK);
    }
}
