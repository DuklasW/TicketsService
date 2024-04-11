package com.example.TicketsService.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class PurchaseTicketRequest {

    @NotBlank
    @Schema(example ="65b5910a8d03341de160c339")
    private String eventId;
    @NotNull
    @Min(1)
    @Schema(example = "2")
    private Integer tickets;

}
