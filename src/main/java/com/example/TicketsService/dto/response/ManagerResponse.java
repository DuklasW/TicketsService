package com.example.TicketsService.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ManagerResponse {
    @Id
    private String id;
    @NotBlank
    private String userId;
    @NotBlank
    private String name;
    @NotBlank
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
    @Pattern(regexp = "^\\[0-9]{2}-[0-9]{3}$", message = "Invalid postcode number")
    private String postcode;
    @NotBlank
    private String regon;
}
