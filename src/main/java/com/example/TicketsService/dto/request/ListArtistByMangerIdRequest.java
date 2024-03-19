package com.example.TicketsService.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ListArtistByMangerIdRequest {

    @Schema(example = "managerId")
    @NotBlank
    private String managerId;
}
