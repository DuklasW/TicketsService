package com.example.TicketsService.model;


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
    private ObjectId menagerId;
    private String publicId;
    private String name;
    private String surname;
    private String nickname;
    private String description;
}
