package com.example.TicketsService.controller;

import com.example.TicketsService.dto.request.CreateArtistRequest;
import com.example.TicketsService.dto.request.ListArtistByMangerIdRequest;
import com.example.TicketsService.dto.response.ArtistResponse;
import com.example.TicketsService.dto.response.ManagerResponse;
import com.example.TicketsService.dto.response.MessageResponse;
import com.example.TicketsService.model.ArtistEntity;
import com.example.TicketsService.model.ManagerEntity;
import com.example.TicketsService.model.UserEntity;
import com.example.TicketsService.security.service.UserDetailsImpl;
import com.example.TicketsService.service.ArtistService;
import com.example.TicketsService.service.ManagerService;
import com.example.TicketsService.service.TokenService;
import com.example.TicketsService.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Tag(name="Manager Controller", description = "Kontroler służący do zadządzania managerami")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/manager")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @Operation(summary = "Pobiera dane wszystkich managerów.",
            description = "Wyświetla listę encji managerów"
    )
    @GetMapping
    public ResponseEntity<List<ManagerResponse>> getAllManager(){
        List<ManagerResponse> managerResponses = managerService.getAllManagerResponses();
        return new ResponseEntity<>(managerResponses, HttpStatus.OK);
    }



    @Operation(summary = "Dodaję artystę do managera.",
            description = "Tworzy encję artysty kóry bedzie przypisany do managera. Trzeba być zalogowany na konto z uprawieniami managera")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @PostMapping("/addartist")
    public ResponseEntity<?> createArtist(@Valid @RequestBody CreateArtistRequest createArtistRequest, Principal principal){

        String managerEmail = principal.getName();
        ArtistEntity artistEntity = managerService.createArtistForManager(managerEmail, createArtistRequest);

        if (artistEntity != null) {
            return ResponseEntity.ok(new MessageResponse("Artist profile created successfully!"));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error while adding artist profile"));
        }
    }

    @Operation(summary = "Wyświetla artystów dowolnego managera.",
            description = "Wyświetla listę encji artystów przypisaną do konkretnego managera, potrzebuje id managera, dostępne dla administracji.")
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @PostMapping("/bymanager")
    public ResponseEntity<List<ArtistResponse>> getArtistByManagerId(@Valid @RequestBody ListArtistByMangerIdRequest request){

        return managerService.getArtistsByManagerId(new ObjectId(request.getManagerId()));
    }

    @Operation(summary = "Wyświetla artystów zalogowanego managera",
            description = "Wyświetla listę encji artystów przypisaną managera który jest zalogowany na podstawie bererToken.")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @PostMapping("/myartist")
    public ResponseEntity<List<ArtistResponse>> getArtistByManagerBererToken(){

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ObjectId userId = userDetails.getId();

        return managerService.getArtistsByManagerId(userId);
    }
}