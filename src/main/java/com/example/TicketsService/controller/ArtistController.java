package com.example.TicketsService.controller;


import com.example.TicketsService.Mapper.CommentMapper;
import com.example.TicketsService.dto.response.CommentResponse;
import com.example.TicketsService.model.ArtistEntity;
import com.example.TicketsService.service.ArtistService;
import com.example.TicketsService.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name="Artist Controller", description = "Kontroler służący do zadządzania artystami")
@RestController
@RequestMapping("/api/artist")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentService commentService;

    //TODO sprawdzić example przy przeniesieniu na serwer
    @Operation(summary = "Wyświetla dane konkretnego artysty.",
            description = "Publiczny endpoint, wyświetla informacje o konkretnym artystście na podstawie jego ID.",
            parameters = {
            @Parameter(name = "artistId", description = "Id artysty", required = true, example = "65b42dcc7e988b05de03b0a3")
            })
    @GetMapping("/{artistId}")
    public ResponseEntity<Optional<ArtistEntity>> getArtistByArtistId(@PathVariable String artistId){
        ObjectId id = new ObjectId(artistId);
        Optional<ArtistEntity> artist = artistService.getArtistByArtistId(id);
        return new ResponseEntity<>(artist, HttpStatus.OK);
    }

    @Operation(summary = "Wyświetla wszystkie komentarze danego artysty.",
    description = "Publiczny endpoint, wyświetla komentarze danego artysty na podstawie jego ID.",
    parameters = {
            @Parameter(name = "artistId", description = "Id artysty", required = true, example = "657b3fd4554e451b737759ed")
    })
    @PostMapping("/artistComments/{artistId}")
    public ResponseEntity<?> showArtistComment(@PathVariable String artistId){
        List<CommentResponse> responses = commentService.getArtistComments(artistId)
                .stream()
                .map(commentMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}