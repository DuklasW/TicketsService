package com.example.TicketsService.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtistResponse {

    @Id
    private String id;
    @NotBlank
    private String managerId;
    @NotBlank
    private String name;
    private String surname;
    private String nickname;
    @NotBlank
    private String description;
    private Boolean isActive;

    public ArtistResponse(ObjectId managerId, String name, String surname, String nickname, String description, Boolean isActive) {
        this.managerId = managerId.toHexString();
        this.name = name;
        this.surname = surname;
        this.nickname = nickname;
        this.description = description;
        this.isActive = isActive;
    }
}
