package com.example.TicketsService.controller;


import com.example.TicketsService.dto.request.CreateArtistRequest;
import com.example.TicketsService.dto.request.ListArtistByMangerIdRequest;
import com.example.TicketsService.model.ArtistEntity;
import com.example.TicketsService.service.ArtistService;
import jakarta.validation.Valid;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/artist")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @GetMapping("/bymanager/{managerId}")
    public ResponseEntity<List<ArtistEntity>> getArtistByManagerId(@PathVariable ObjectId managerId) {
        List<ArtistEntity> artists = artistService.getArtistByManagerId(managerId);
            return new ResponseEntity<>(artists, HttpStatus.OK);
    }

    @PostMapping("/bymanager")
    public ResponseEntity<List<ArtistEntity>> getArtistByManagerIdd(@Valid @RequestBody ListArtistByMangerIdRequest request){

        ObjectId objectManagerId = new ObjectId(request.getManagerId());
        List<ArtistEntity> artists = artistService.getArtistByManagerId(objectManagerId);
        return new ResponseEntity<>(artists, HttpStatus.OK);
    }



}

