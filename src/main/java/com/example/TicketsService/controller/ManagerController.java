package com.example.TicketsService.controller;

import com.example.TicketsService.dto.request.CreateArtistRequest;
import com.example.TicketsService.dto.request.ListArtistByMangerIdRequest;
import com.example.TicketsService.dto.response.ManagerResponse;
import com.example.TicketsService.dto.response.MessageResponse;
import com.example.TicketsService.model.ArtistEntity;
import com.example.TicketsService.security.service.UserDetailsImpl;
import com.example.TicketsService.service.ManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@Tag(name="Manager Controller", description = "Kontroler służący do zadządzania managerami")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/manager")
public class ManagerController {

    private final ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @Operation(summary = "Pobiera dane wszystkich managerów.",
            description = "Wyświetla listę encji managerów, tylko dla administracji"
    )
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<ManagerResponse>> getAllManager(){
        List<ManagerResponse> managerResponses = managerService.getAllManagerResponses();
        return new ResponseEntity<>(managerResponses, HttpStatus.OK);
    }


    @Operation(summary = "Dodaję artystę do managera.",
            description = "Tworzy encję artysty kóry bedzie przypisany do managera. Trzeba być zalogowany na konto z uprawieniami managera")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
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
    public ResponseEntity<?> getArtistByManagerId(@RequestBody ListArtistByMangerIdRequest request){
        return managerService.getArtistsByManagerId(new ObjectId(request.getManagerId()));
    }

    @Operation(summary = "Wyświetla artystów zalogowanego managera",
            description = "Wyświetla listę encji artystów przypisaną managera który jest zalogowany na podstawie bererToken.")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("/myartist")
    public ResponseEntity<?> getArtistByManagerBererToken(){

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ObjectId userId = userDetails.getId();

        return managerService.getArtistsByUserId(userId);
    }
}