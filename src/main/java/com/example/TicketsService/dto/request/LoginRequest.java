package com.example.TicketsService.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @Schema(example = "consumer2@consumer.com")
    @NotBlank
    @Email
    private String email;

    @Schema(example = "password")
    @NotBlank
    @Size(min = 8, message = "Password too short")
    private String password;
}
