package com.example.TicketsService.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseAcceptRequest {
    private String token;
    private String payerId;
}
