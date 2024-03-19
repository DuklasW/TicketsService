package com.example.TicketsService.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
    @NotBlank
    @Schema(example = "657b3fd4554e451b737759ed")
    private String artistId;
    @NotBlank
    @Schema(example = "Najlepszy koncert na świecie!")
    private String description;
    @Min(value = 0, message = "Value must be greater than or equal to 0")
    @Max(value = 10, message = "Value must be less than or equal to 10")
    @Schema(example = "7")
    private int stars;
}
