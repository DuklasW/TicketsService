package com.example.TicketsService.dto.request;

import com.example.TicketsService.model.enums.RegonPolandEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.Set;


@Getter
@Setter
public class SignUpConsumerRequest extends SignUpRequest{

    @Schema(example = "Jan")
    @NotBlank
    private String name;

    @Schema(example = "Testowy")
    @NotBlank
    private String surname;

    @Schema(example = "123456789")
    @NotBlank
    @Pattern(regexp = "^[0-9]{9}$", message = "Invalid phone number")
    private String phone;

    @Schema(example = "Kujawsko-Pomorskie",
            title = "Województwa są ustawione na sztywno",
            description = "lista przykładowych: Dolnośkąskie, Kujawsko-Pomorskie, Lubelskie, Lubuskie, Łódzkie, " +
                    "Małopolskie, Mazowieckie, Opolskie, Podkarpackie, Podlaskie, Pomorskie, Śląskie, Świętkorzyskie," +
                    "Warmińsko-Mazurskie, Wielkopolskie, Zachodniopomorskie")
    @NotBlank
    private String regon;

    @Schema(example = "Toruń")
    private String city;
    @Schema(example = "87-100")
    @Pattern(regexp = "(^[0-9]{2}-[0-9]{3}$)?", message = "Invalid postcode number")
    private String postcode;
    @Schema(example = "Pieskowa 18")
    private String street;

    @JsonIgnore
    public boolean isValidRegon(){
        return RegonPolandEnum.formDisplayRegon(regon) != null;
    }

    @JsonIgnore
    @Override
    public Set<String> getRoles(){
        return Collections.singleton("ROLE_CONSUMER");
    }


    //TODO Tylko do przejrzystszego tworzenia użytkownika w swaggerze - usunąć później pole email
    @Schema(example = "jasiek3@user.com" )
    @Override
    public String getEmail() {
        return super.getEmail();
    }

    //Nie ma userID, companyCity, companyName, companyNip, companyPostcode, companyStreet
}

