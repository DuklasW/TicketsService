package com.example.TicketsService.controller;

import com.example.TicketsService.dto.request.*;
import com.example.TicketsService.dto.response.JwtResponse;
import com.example.TicketsService.dto.response.MessageResponse;
import com.example.TicketsService.dto.response.TokenRefreshResponse;
import com.example.TicketsService.model.ConsumerEntity;
import com.example.TicketsService.model.ManagerEntity;
import com.example.TicketsService.model.RefreshTokenEntity;
import com.example.TicketsService.model.UserEntity;
import com.example.TicketsService.model.enums.RoleEnum;
import com.example.TicketsService.security.jwt.JwtUtils;
import com.example.TicketsService.security.service.UserDetailsImpl;
import com.example.TicketsService.security.service.RefreshTokenService;
import com.example.TicketsService.service.ConsumerService;
import com.example.TicketsService.service.ManagerService;
import com.example.TicketsService.service.UserService;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.TicketsService.exception.TokenRefreshException;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Validated
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    UserService userService;

    @Autowired
    ManagerService managerService;

    @Autowired
    ConsumerService consumerService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    //logowanie uzytkownika, zwaraca parę tokenów, id i rolę uzytkownika
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);


        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(), userDetails.getEmail(), roles));
    }

    //podstawowa rejestracja
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signupRequest){

        try {
            if(userService.checkUserExistByEmail(signupRequest.getEmail())){
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Email is already taken"));
            }

            //create new user's account

            Set<String> stringRoles = signupRequest.getRoles();
            Set<RoleEnum> userRoles;

            if (stringRoles == null || stringRoles.isEmpty()) {
                //primary role is consumer
                userRoles = new HashSet<>(Collections.singletonList(RoleEnum.ROLE_CONSUMER));
            } else {
                userRoles = stringRoles.stream()
                        .map(role -> {
                            try {
                                return RoleEnum.valueOf("ROLE_" + role.toUpperCase());
                            } catch (IllegalArgumentException e) {
                                throw new RuntimeException("Error: Role is not found.");
                            }
                        })
                        .collect(Collectors.toSet());
            }

            UserEntity userEntity = new UserEntity(signupRequest.getEmail(), encoder.encode(signupRequest.getPassword()), userRoles);


            if( userService.save(userEntity) != null) {
                return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new MessageResponse("Error while saving the user"));
            }

        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error occured: " + e.getMessage()));
        }


    }

    //rejestracja zwykłego użytkownika
    @PostMapping("/signup-consumer")
    public ResponseEntity<?> registerConsumer(@Valid @RequestBody SignUpConsumerRequest signUpConsumerRequest){

        try {
            if(userService.checkUserExistByEmail(signUpConsumerRequest.getEmail())){
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Email is already taken"));
            }
            //TODO przetestwoać
            if(!signUpConsumerRequest.isValidRegon()){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new MessageResponse("Error, region not in enum list"));
            }

            //create new consumer's account
            Set<RoleEnum> userRoles = new HashSet<>(Collections.singletonList(RoleEnum.ROLE_CONSUMER));
            UserEntity userEntity = new UserEntity(signUpConsumerRequest.getEmail(), encoder.encode(signUpConsumerRequest.getPassword()), userRoles);

            if( userService.save(userEntity) != null) {
                //create consumer profile
                ConsumerEntity consumerEntity = new ConsumerEntity(userEntity.getIdAsObjectId(), signUpConsumerRequest.getName(), signUpConsumerRequest.getSurname(), signUpConsumerRequest.getCity(), signUpConsumerRequest.getPhone(), signUpConsumerRequest.getPostcode(), signUpConsumerRequest.getRegon(), signUpConsumerRequest.getStreet());

                        if(consumerService.save(consumerEntity) != null){
                            return ResponseEntity.ok(new MessageResponse("Consumer registered successfully!"));
                        }else{
                            userService.deleteByUserId(userEntity.getIdAsObjectId());
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body(new MessageResponse("Error while saving consumer profile"));
                        }

            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new MessageResponse("Error while saving the user"));
            }

        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error occured: " + e.getMessage()));
        }
    }

    //rejestracja managera
    @PostMapping("/signup-manager")
    public ResponseEntity<?> registerManager(@Valid @RequestBody SignUpManagerRequest signUpManagerRequest){

        try {
            if(userService.checkUserExistByEmail(signUpManagerRequest.getEmail())){
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Email is already taken"));
            }

            //create new manager's account
            Set<RoleEnum> userRoles = new HashSet<>(Collections.singletonList(RoleEnum.ROLE_MANAGER));
            UserEntity userEntity = new UserEntity(signUpManagerRequest.getEmail(), encoder.encode(signUpManagerRequest.getPassword()), userRoles);

            if( userService.save(userEntity) != null) {
                //create consumer profile
                ManagerEntity managerEntity = new ManagerEntity(userEntity.getIdAsObjectId(), signUpManagerRequest.getName(), signUpManagerRequest.getCheckVat(), signUpManagerRequest.getCity(), signUpManagerRequest.getCompanyName(), signUpManagerRequest.getCompanyStreet(), signUpManagerRequest.getNip(), signUpManagerRequest.getPhone(), signUpManagerRequest.getPostcode(), signUpManagerRequest.getRegon());

                if(managerService.save(managerEntity) != null){
                    return ResponseEntity.ok(new MessageResponse("Manager registered successfully!"));
                }else{
                    userService.deleteByUserId(userEntity.getIdAsObjectId());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new MessageResponse("Error while saving manager profile"));
                }

            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new MessageResponse("Error while saving the user"));
            }

        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error occured: " + e.getMessage()));
        }


    }

    //służy do uzyskania nowego tokenu na podstawie refreshToken
    //po wysłaniu refreshToken sprawdza czy znajduje się taki w bazie danych, następnie go usuwa i tworzy nową parę tokenów
    @PostMapping("refreshtoken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request){
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshTokenEntity::getUserEntity)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromEmail(user.getEmail());
                    refreshTokenService.deleteByUserId(user.getIdAsObjectId());
                    RefreshTokenEntity newRefreshToken = refreshTokenService.createRefreshToken(user.getIdAsObjectId());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, newRefreshToken.getToken()));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database"));
    }

    //usuwa refreshToken
    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ObjectId userId = userDetails.getId();
        refreshTokenService.deleteByUserId(userId);
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
    }
}
