package com.example.TicketsService.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.Date;

@Setter
@Getter
public class PurchaseReponse {
    private String id;
    private String eventId;
    private Date date;
    private String consumerId;
    private Integer tickets;
    private String payPalId;
    private String statusPay;
}
