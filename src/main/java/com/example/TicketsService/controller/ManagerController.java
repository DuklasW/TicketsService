package com.example.TicketsService.controller;

import com.example.TicketsService.dto.request.CreateArtistRequest;
import com.example.TicketsService.dto.response.MessageResponse;
import com.example.TicketsService.model.ArtistEntity;
import com.example.TicketsService.model.ManagerEntity;
import com.example.TicketsService.model.UserEntity;
import com.example.TicketsService.security.jwt.JwtUtils;
import com.example.TicketsService.service.ArtistService;
import com.example.TicketsService.service.ManagerService;
import com.example.TicketsService.service.TokenService;
import com.example.TicketsService.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/manager")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;


    @GetMapping
    public ResponseEntity<List<ManagerEntity>> getAllManager(){
        return new ResponseEntity<List<ManagerEntity>>(managerService.allManagers(), HttpStatus.OK);
    }

    @PostMapping("/addartist")
    public ResponseEntity<?> createArtist(@Valid @RequestBody CreateArtistRequest createArtistRequest, HttpServletRequest request){

        try {
                String managerEmail = tokenService.getEmailFromHttpServletRequest(request);
                Optional<UserEntity> userEntityOptional = userService.getUserByUserEmail(managerEmail);


                if(!userEntityOptional.isPresent()) {
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new MessageResponse("Error: don't find user by email from Token"));
                }

                ObjectId userId = new ObjectId(userEntityOptional.get().getId());
                Optional<ManagerEntity> managerEntityOptional = managerService.getManagerByUserId(userId);


                if(!managerEntityOptional.isPresent()) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new MessageResponse("Error: don't find managerEntity from userId"));
                }

                ArtistEntity artistEntity = new ArtistEntity(managerEntityOptional.get().getIdByObjectID(), createArtistRequest.getName(), createArtistRequest.getSurname(), createArtistRequest.getNickname(), createArtistRequest.getDescription(), true);

                if(artistService.save(artistEntity) != null) {
                    return ResponseEntity.ok(new MessageResponse("Artist profile created successfully!"));
                }else {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new MessageResponse("Error while adding artist profile"));
                }


        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error occured: " + e.getMessage()));
        }
    }
}