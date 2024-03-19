package com.example.TicketsService.controller;

import com.example.TicketsService.model.UserEntity;
import com.example.TicketsService.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Tag(name="User Controller", description = "Kontroler służący do sprawdzania użytkowników, tylko dla administracji")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/test/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Wyświetl użytkowników", description = "Pozwala wyświetlić listę użytkowników, tylko dla administracji")
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserEntity>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }
    @Operation(summary = "Wyświetl użytkownika po ID", description = "Wyświetla użytkownika o podanym id, tylko dla administracji",
    parameters = {
            @Parameter(name = "userId", description = "Id użytkownika", required = true, example = "65b2d492a162224d2a3e957d")
    })
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping("/{userId}")
    public  ResponseEntity<Optional<UserEntity>> getSingleUser(@PathVariable String userId){
        return new ResponseEntity<>(userService.getUserByUserId(new ObjectId(userId)), HttpStatus.OK);
    }
    @Operation(summary = "Sprawdzenie czy email jest zajęty", description = "Pozwala na sprawdzenie, czy istnieje użytkownik o podanym email, tylko dla administracji",
            parameters = {
                    @Parameter(name = "email", description = "Email użytkownika", required = true, example = "consumer@consumer.com")
            })
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping("/email/{email}")
    public ResponseEntity<Boolean> checkUserExist(@PathVariable String email){
        return new ResponseEntity<>(userService.checkUserExistByEmail(email), HttpStatus.OK);
    }
}
