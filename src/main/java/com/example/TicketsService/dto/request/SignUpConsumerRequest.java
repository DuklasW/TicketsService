package com.example.TicketsService.dto.request;

import com.example.TicketsService.model.enums.RegonPolandEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SignUpConsumerRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, message = "Password too short!")
    private String password;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    @Pattern(regexp = "^\\[0-9]{9}$", message = "Invalid phone number")
    private String phone;

    @NotBlank
    private String regon;

    private String city;
    @Pattern(regexp = "(^[0-9]{2}-[0-9]{3}$)?", message = "Invalid postcode number")
    private String postcode;
    private String street;

    public boolean isValidRegon(){
        return RegonPolandEnum.formDisplayRegon(regon) != null;
    }

    //Nie ma userID, companyCity, companyName, companyNip, companyPostcode, companyStreet
}

