package com.example.TicketsService.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private String refreshToken;
    private String type = "Bearer";
    private String id;
    private String email;
    private List<String> roles;

    public JwtResponse(String accessToken, String refreshToken, ObjectId id, String email, List<String> roles){
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.id = id.toHexString();
        this.email = email;
        this.roles = roles;
    }
}
