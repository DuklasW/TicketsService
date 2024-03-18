package com.example.TicketsService.controller;

import com.example.TicketsService.dto.request.*;
import com.example.TicketsService.dto.response.JwtResponse;
import com.example.TicketsService.dto.response.MessageResponse;
import com.example.TicketsService.dto.response.TokenRefreshResponse;
import com.example.TicketsService.model.*;
import com.example.TicketsService.model.enums.RoleEnum;
import com.example.TicketsService.security.jwt.JwtUtils;
import com.example.TicketsService.security.service.UserDetailsImpl;
import com.example.TicketsService.security.service.RefreshTokenService;
import com.example.TicketsService.service.AuthService;
import com.example.TicketsService.service.ConsumerService;
import com.example.TicketsService.service.ManagerService;
import com.example.TicketsService.service.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
@Tag(name="Auth Controller", description = "Kontroler służący do zadządzania procesem rejestracji oraz logowania")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    AuthService authService;

    @Operation(summary = "Logowanie",
            description = "Publiczny endpoint, pozwala się zalogować na wybrane konto. " +
                    "Zwraca podstawowe informacje o użytkowniku wraz z tokenem JWT. " +
                    "W przypadku niepowodzenia autentykacji zwraca kod błędu 401. " +
                    "Przykładowe konta: ..., ... , ... ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - returns basic information about the user.",
                    content = { @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserEntity.class)) }),
            @ApiResponse(responseCode = "401", description = "Authentication failed. Please check your credentials."),
    })
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
       return authService.authenticateUser(loginRequest);
    }


    @Operation(summary = "Rejestracja dla administratorów",
            description = "Endpoint rejestracji, który umożliwia administratorom nadanie dowolnych uprawnień nowym użytkownikom " +
                    "trzeba podać tylko email, hasło oraz role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - User registered successfully!"),
            @ApiResponse(responseCode = "400", description = "Error: Email is already taken.")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signupRequest){
        return authService.registerAnyUser(signupRequest);
    }



    @Operation(summary = "Rejestracja użytkownika",
            description = "Endpoint rejestracji zwykłego użytkownika")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - User registered successfully!"),
            @ApiResponse(responseCode = "400", description = "Error: Email is already taken.")
    })
    @PostMapping("/signup-consumer")
    public ResponseEntity<?> registerConsumer(@Valid @RequestBody SignUpConsumerRequest signUpConsumerRequest){
        return authService.registerConsumer(signUpConsumerRequest);
    }

    @Operation(summary = "Rejestracja managera",
            description = "Endpoint rejestracji konta managera")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Manager registered successfully!"),
            @ApiResponse(responseCode = "400", description = "Error: Email is already taken.")
    })
    @PostMapping("/signup-manager")
    public ResponseEntity<?> registerManager(@Valid @RequestBody SignUpManagerRequest signUpManagerRequest){
        return authService.registerManager(signUpManagerRequest);
    }

    @Operation(summary = "Odświeżanie tokenu",
            description = "Endpoint służący do uzyskania nowego tokenu na podstawie refreshToken. " +
                    "Po wysłaniu refreshToken sprawdza czy znajduje się taki w bazie danych, następnie go usuwa i tworzy nową parę tokenów")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Token refreshed successfully!"),
            @ApiResponse(responseCode = "400", description = "Error: Refresh token is not in database")
    })
    @PostMapping("refreshtoken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request){
        return refreshTokenService.refreshToken(request);
    }

    @Operation(summary = "Wylogowanie",
            description = "Endpoint służący do wylogowania użytkownika czyli usuwa refreshToken z bazy danych.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK - Log out successful!"),
            @ApiResponse(responseCode = "401", description = "Error, you are not logged on, send current bererToken!")
    })
    @DeleteMapping("/signout")
    public ResponseEntity<?> logoutUser(){
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            ObjectId userId = userDetails.getId();
            refreshTokenService.deleteByUserId(userId);
            return ResponseEntity.ok(new MessageResponse("Log out successful!"));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Error, you are not logged on, send current bererToken!"));
        }
    }
}
