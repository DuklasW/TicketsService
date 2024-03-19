package com.example.TicketsService.model;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "artists")
public class ArtistEntity {
    @Id
    private ObjectId id;
    @NotBlank
    @Field(targetType = FieldType.OBJECT_ID)
    private String managerId;
    @NotBlank
    private String name;
    private String surname;
    private String nickname;
    @NotBlank
    private String description;
    private Boolean isActive;

    public ArtistEntity(String managerId, String name, String surname, String nickname, String description, Boolean isActive) {
        this.managerId = managerId;
        this.name = name;
        this.surname = surname;
        this.nickname = nickname;
        this.description = description;
        this.isActive = isActive;
    }
}