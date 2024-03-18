package com.example.TicketsService.service;

import com.example.TicketsService.dto.request.*;
import com.example.TicketsService.dto.response.JwtResponse;
import com.example.TicketsService.dto.response.MessageResponse;
import com.example.TicketsService.dto.response.TokenRefreshResponse;
import com.example.TicketsService.exception.TokenRefreshException;
import com.example.TicketsService.model.ConsumerEntity;
import com.example.TicketsService.model.ManagerEntity;
import com.example.TicketsService.model.RefreshTokenEntity;
import com.example.TicketsService.model.UserEntity;
import com.example.TicketsService.model.enums.RoleEnum;
import com.example.TicketsService.security.jwt.JwtUtils;
import com.example.TicketsService.security.service.RefreshTokenService;
import com.example.TicketsService.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

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

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticate(loginRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            String jwt = jwtUtils.generateJwtToken(userDetails);


            List<String> roles = userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

            return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(), userDetails.getEmail(), roles));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Authentication failed. Please check your credentials."));
        }
    }

    private Authentication authenticate(LoginRequest loginRequest) {
        return authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
                );
    }

    public ResponseEntity<?> registerAnyUser(SignUpRequest signUpRequest) {
        try {
            if (userService.checkUserExistByEmail(signUpRequest.getEmail())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Email is already taken"));
            }

            //create new user's account

            Set<String> stringRoles = signUpRequest.getRoles();
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

            UserEntity userEntity = new UserEntity(signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()), userRoles);


            if (userService.save(userEntity) != null) {
                return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new MessageResponse("Error while saving the user"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error occured: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> registerConsumer(SignUpConsumerRequest signUpConsumerRequest) {
        try {
            validateUser(signUpConsumerRequest);

            //create new user account for consumer
            UserEntity userEntity = createUser(signUpConsumerRequest, RoleEnum.ROLE_CONSUMER);

            //create consumer profile
            createConsumerProfile(signUpConsumerRequest, userEntity);


            return ResponseEntity.ok(new MessageResponse("Consumer registered successfully!"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error occured: " + e.getMessage()));
        }
    }

    private void createConsumerProfile(SignUpConsumerRequest signUpConsumerRequest, UserEntity userEntity) throws Exception {
        ConsumerEntity consumerEntity = new ConsumerEntity(userEntity.getIdAsObjectId(), signUpConsumerRequest.getName(), signUpConsumerRequest.getSurname(), signUpConsumerRequest.getCity(), signUpConsumerRequest.getPhone(), signUpConsumerRequest.getPostcode(), signUpConsumerRequest.getRegon(), signUpConsumerRequest.getStreet());

        if (consumerService.save(consumerEntity) == null) {
            userService.deleteByUserId(userEntity.getIdAsObjectId());
            throw new Exception("Error while saving consumer profile");
        }
    }

    public ResponseEntity<?> registerManager(SignUpManagerRequest signUpManagerRequest) {
        try {
            validateUser(signUpManagerRequest);

            //create new user account for manager
            UserEntity userEntity = createUser(signUpManagerRequest, RoleEnum.ROLE_MANAGER);

            //create manager profile
            createManagerProfile(signUpManagerRequest, userEntity);

            return ResponseEntity.ok(new MessageResponse("Manager registered successfully!"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error occured: " + e.getMessage()));
        }
    }

    private UserEntity createUser(SignUpRequest signUpRequest, RoleEnum ROLE) throws Exception {
        Set<RoleEnum> userRoles = new HashSet<>(Collections.singletonList(ROLE));
        UserEntity userEntity = new UserEntity(signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()), userRoles);

        if (userService.save(userEntity) == null) {
            throw new Exception("Error while saving the user");
        }
        return userEntity;
    }

    private void validateUser(SignUpRequest signUpRequest) throws Exception {
        if (userService.checkUserExistByEmail(signUpRequest.getEmail())) {
            throw new Exception("Error: Email is already taken");
        }
        if (!signUpRequest.isValidRegon()) {
            throw new Exception("Error,, regon not in enum list");
        }
    }

    private void createManagerProfile(SignUpManagerRequest signUpManagerRequest, UserEntity userEntity) throws Exception {
        ManagerEntity managerEntity = new ManagerEntity(userEntity.getIdAsObjectId(), signUpManagerRequest.getName(), signUpManagerRequest.getCheckVat(), signUpManagerRequest.getCity(), signUpManagerRequest.getCompanyName(), signUpManagerRequest.getCompanyStreet(), signUpManagerRequest.getNip(), signUpManagerRequest.getPhone(), signUpManagerRequest.getPostcode(), signUpManagerRequest.getRegon());

        if (managerService.save(managerEntity) == null) {
            userService.deleteByUserId(userEntity.getIdAsObjectId());
            throw new Exception("Error while saving consumer profile");
        }
    }
}
