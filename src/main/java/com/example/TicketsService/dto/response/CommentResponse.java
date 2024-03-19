package com.example.TicketsService.dto.response;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentResponse {

    private String id;
    private String artistId;
    private String description;
    private int stars;
}
