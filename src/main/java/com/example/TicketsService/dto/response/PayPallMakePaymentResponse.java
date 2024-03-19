package com.example.TicketsService.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PayPallMakePaymentResponse {
    private String id;
    private String status;
    private List<Link> links;
}