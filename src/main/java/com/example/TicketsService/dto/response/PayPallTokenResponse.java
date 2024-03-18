package com.example.TicketsService.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayPallTokenResponse {
    private String access_token;
    private int expires_in;
    private String token_type;
    private String scope;
}
