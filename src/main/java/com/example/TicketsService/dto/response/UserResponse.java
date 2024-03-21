package com.example.TicketsService.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import java.util.List;

@Getter
@Setter
public class UserResponse {
    private String id;
    private String email;
    private String password;
    private List<String> roles;
}
