package com.example.TicketsService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "purchases")
public class PurchaseEntity {

    @Id
    @Field(targetType = FieldType.OBJECT_ID)
    private String id;
    @Field(targetType = FieldType.OBJECT_ID)
    private String eventId;
    private Date date;
    @Field(targetType = FieldType.OBJECT_ID)
    private String consumerId;
    private Integer tickets;
    private String payPalId;
    private String statusPay;

    public PurchaseEntity(String eventId, Date date, ObjectId consumerId, Integer tickets, String payPalId, String statusPay) {
        this.eventId = eventId;
        this.date = date;
        this.consumerId = consumerId.toHexString();
        this.tickets = tickets;
        this.payPalId = payPalId;
        this.statusPay = statusPay;
    }
}