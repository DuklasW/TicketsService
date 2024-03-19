package com.example.TicketsService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "comments")
public class CommentEntity {
    @Id
    private ObjectId id;
    @Field(targetType = FieldType.OBJECT_ID)
    private String artistId;
    @Field(targetType = FieldType.OBJECT_ID)
    private String consumerId;
    private Integer stars;
    private String description;

    public CommentEntity(String artistId, String consumerId, Integer stars, String description) {
        this.artistId = artistId;
        this.consumerId = consumerId;
        this.stars = stars;
        this.description = description;
    }
}
