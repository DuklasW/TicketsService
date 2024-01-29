package com.example.TicketsService.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PurchaseTicketRequest {

    @NotBlank
    private String eventId;
    @NotNull
    private Integer tickets;

}
