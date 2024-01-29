package com.example.TicketsService.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
public class CreateArtistRequest {

    @NotBlank
    private String name;

    private String surname;

    private String nickname;

    @NotBlank
    private String description;

}
