package com.example.TicketsService.model;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "artists")
public class ArtistEntity {
    @Id
    private ObjectId id;
    @NotBlank
    private ObjectId managerId;
    @NotBlank
    private String name;
    private String surname;
    private String nickname;
    @NotBlank
    private String description;
    private Boolean isActive;

    public ArtistEntity(ObjectId managerId, String name, String surname, String nickname, String description, Boolean isActive) {
        this.managerId = managerId;
        this.name = name;
        this.surname = surname;
        this.nickname = nickname;
        this.description = description;
        this.isActive = isActive;
    }
}

//    public ArtistEntity(ObjectId id, ObjectId managerId, String name, String surname, String nickname, String description, Boolean isActive) {
//        this.id = id;
//        this.managerId = managerId;
//        this.name = name;
//        this.surname = surname;
//        this.nickname = nickname;
//        this.description = description;
//        this.isActive = isActive;
//    }
//}
