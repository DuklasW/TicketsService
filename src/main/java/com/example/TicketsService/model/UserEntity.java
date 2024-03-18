package com.example.TicketsService.model;
import com.example.TicketsService.model.enums.RoleEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "users")
public class UserEntity {
    @Id
    private String id;
    @Indexed(unique = true)
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min=8)
    private String password;

    @Field("roles")
    private List<String> roles;

    @Field("refreshToken")
    private RefreshTokenEntity refreshToken;

    @JsonIgnore
    public UserEntity(ObjectId id, String email, String password, List<String> roles) {
        this.id = id.toHexString();
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    @JsonIgnore
    public UserEntity(String email, String password, Set<RoleEnum> roles){
        this.email = email;
        this.password = password;
        this.roles = new ArrayList<>(roles.stream().map(RoleEnum::name).collect(Collectors.toList()));
    }

    @JsonIgnore
    public UserEntity(String email, String password, Set<RoleEnum> roles, RefreshTokenEntity refreshToken){
        this.email = email;
        this.password = password;
        this.roles = new ArrayList<>(roles.stream().map(RoleEnum::name).collect(Collectors.toList()));
        this.refreshToken = refreshToken;
    }

    @JsonIgnore
    public Set<RoleEnum> getRoles() {
        return new HashSet<>(roles.stream().map(RoleEnum::valueOf).collect(Collectors.toList()));
    }

    @JsonIgnore
    public ObjectId getIdAsObjectId(){
        return new ObjectId(this.id);
    }
}
