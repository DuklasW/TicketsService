package com.example.TicketsService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "comments")
public class CommentEntity {
    @Id
    private ObjectId id;
    private ObjectId artistId;
    private ObjectId consumerId;
    private Double stars;
    private String description;

}
