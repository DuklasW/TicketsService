package com.example.TicketsService.controller;

import com.example.TicketsService.model.UserEntity;
import com.example.TicketsService.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/test/user")
public class UserController {

    @Autowired
    private UserService userService;
    @GetMapping
    public ResponseEntity<List<UserEntity>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<Optional<UserEntity>> getSingleUser(@PathVariable ObjectId id){
        return new ResponseEntity<>(userService.getUserByUserId(id), HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Boolean> checkUserExist(@PathVariable String email){
        return new ResponseEntity<>(userService.checkUserExistByEmail(email), HttpStatus.OK);
    }
}
