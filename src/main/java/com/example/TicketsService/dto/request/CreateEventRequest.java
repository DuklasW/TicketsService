package com.example.TicketsService.dto.request;

import com.example.TicketsService.model.enums.RegonPolandEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.util.List;

@Setter
@Getter
public class CreateEventRequest {

    private List<Date> dates;
    @Schema( example = "[\"Krzysztof\",\"Jan\"]")
    private List<String> artistName;

    @Schema(example = "10")
    @NotNull
    @DecimalMin(value = "0.0", message = "Price must be greater than or equal to 0 ")
    private Double price;

    @Schema(example = "100")
    @NotNull
    @DecimalMin(value = "0", inclusive = false, message = "Number of tickets must be greater than 0")
    private Integer ticketsNumber;

    @Schema(example = "Bar pod mikrofonem")
    @NotBlank
    private String location;

    @Schema(example = "Toruń")
    @NotBlank
    private String city;

    @Schema(example = "87-100")
    @NotBlank
    @Pattern(regexp = "^[0-9]{2}-[0-9]{3}$", message = "Invalid postcode number")
    private String postcode;

    @Schema(example = "kujawsko-pomorskie")
    @NotBlank
    private String regon;

    @Schema(example = "Starówkowa")
    @NotBlank
    private String street;

    @Schema(example = "Wielkie wydarzenie komików")
    @NotBlank
    private String name;

    @Schema(example = "Wielki miks 3 wyjątkowych standuperów, którzy pokażą wspólny program")
    @NotBlank
    private String description;

    @Schema(example = "65b42dcc7e988b05de03b0a3")
    @NotBlank
    private String createdByArtist;

    @JsonIgnore
    public boolean isValidRegon(){
        return RegonPolandEnum.formDisplayRegon(regon) != null;
    }

}
