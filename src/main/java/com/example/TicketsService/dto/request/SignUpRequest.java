package com.example.TicketsService.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SignUpRequest {

    @Schema(example = "admin2@user.pl")
    @NotBlank
    @Email
    protected String email;

    @Schema(example = "Passwordini123@")
    @NotBlank
    @Size(min=8, message = "Password too short!")
    protected String password;

    @Schema( example = "[\"ADMIN\"]")
    protected Set<String> roles;

    @JsonIgnore
    public boolean isValidRegon() {
        throw new UnsupportedOperationException("Method isValidRegon is not supported for this user type");
    }
}