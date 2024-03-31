package com.example.TicketsService.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "refreshToken")
public class RefreshTokenEntity {

    @Id
    private ObjectId id;

    @DBRef
    private UserEntity userEntity;

    @NotBlank
    private String token;

    @NotBlank
    private Instant expiryDate;
}
