package com.example.TicketsService.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpManagerRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, message = "Password too short")
    private String password;

    @NotBlank
    private String name;
    
    private Boolean checkVat;

    @NotBlank
    private String city;

    @NotBlank
    private String companyName;

    @NotBlank
    private String companyStreet;

    @NotBlank
    @Pattern(regexp = "d{3}-?\\d{2,3}-?\\d{2,3}-?\\d{2,3}", message = "Invalid nip number")
    private String nip;

    @NotBlank
    @Pattern(regexp = "^\\[0-9]{9}$", message = "Invalid phone number")
    private String phone;

    @NotBlank
    @Pattern(regexp = "^[0-9]{2}-[0-9]{3}$", message = "Invalid postcode number")
    private String postcode;

    @NotBlank
    private String regon;
}
