package com.example.TicketsService.model;
//TODO dodać kubernetis, po stworzeniu pierwszego połączenia z bazą
//TODO dalej kontynuować CQRS(raczej idziemy z CRUD), REST, paypal jako sandobx?
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "users")
public class UserEntity {
    @Id
    private ObjectId id;
    @Indexed(unique = true)
    @NonNull
    private String email;
    @NonNull
    private String password;
    private String role;
}
