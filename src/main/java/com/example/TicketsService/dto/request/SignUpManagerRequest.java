package com.example.TicketsService.dto.request;

import com.example.TicketsService.model.enums.RegonPolandEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import java.util.Collections;
import java.util.Set;

@Getter
@Setter
public class SignUpManagerRequest extends SignUpRequest{

    @Schema(example = "Krzysztof")
    @NotBlank
    private String name;

    @Schema(example = "false")
    private Boolean checkVat;

    @Schema(example = "Warszawa")
    @NotBlank
    private String city;

    @Schema(example = "Wytwórnia Artystów")
    @NotBlank
    private String companyName;

    @Schema(example = "Wymyślna 15")
    @NotBlank
    private String companyStreet;

    @Schema(example = "022-41-11-111")
    @NotBlank
    @Pattern(regexp = "\\d{3}-?\\d{2,3}-?\\d{2,3}-?\\d{2,3}", message = "Invalid nip number")
    private String nip;

    @Schema(example = "111222333")
    @NotBlank
    @Pattern(regexp = "^[0-9]{9}$", message = "Invalid phone number")
    private String phone;

    @Schema(example = "00-001")
    @NotBlank
    @Pattern(regexp = "^[0-9]{2}-[0-9]{3}$", message = "Invalid postcode number")
    private String postcode;

    @Schema(example = "Mazowieckie",
            title = "Województwa są ustawione na sztywno",
            description = "lista przykładowych: Dolnośkąskie, Kujawsko-Pomorskie, Lubelskie, Lubuskie, Łódzkie, " +
                    "Małopolskie, Mazowieckie, Opolskie, Podkarpackie, Podlaskie, Pomorskie, Śląskie, Świętkorzyskie," +
                    "Warmińsko-Mazurskie, Wielkopolskie, Zachodniopomorskie")
    @NotBlank
    private String regon;

    @JsonIgnore
    public boolean isValidRegon(){
        return RegonPolandEnum.formDisplayRegon(regon) != null;
    }

    @JsonIgnore
    @Override
    public Set<String> getRoles(){
        return Collections.singleton("ROLE_MANAGER");
    }

    //TODO Tylko do przejrzystszego tworzenia użytkownika w swaggerze - usunąć później pole email
    @Schema(example = "Manager.dody@user.pl")
    @Override
    public String getEmail() {
        return email;
    }
}
