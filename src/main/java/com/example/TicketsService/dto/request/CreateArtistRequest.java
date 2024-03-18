package com.example.TicketsService.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
public class CreateArtistRequest {

    @Schema(example = "Krzysztof")
    @NotBlank
    private String name;

    @Schema(example = "Krzak")
    private String surname;

    @Schema(example = "Zawłoszy")
    private String nickname;

    @Schema(example = "Najlepsza nowoczesna gwiazda muzyki klasycznej, łaczy w humor z niewyobrażalnym talentem!")
    @NotBlank
    private String description;

}
