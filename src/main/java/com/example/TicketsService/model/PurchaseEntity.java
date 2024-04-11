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

    //TODO ZMIANA ZE String na ObjectId
    @Id
    private ObjectId id;
    @Field(targetType = FieldType.OBJECT_ID)
    private String eventId;
    private Date date;
    @Field(targetType = FieldType.OBJECT_ID)
    private String consumerId;
    private Integer tickets;
    private String payPalId;
    private String statusPay;
    private Date expiredDate;

    public PurchaseEntity(String eventId, Date date, ObjectId consumerId, Integer tickets, String payPalId, String statusPay, Date expiredDate) {
        this.eventId = eventId;
        this.date = date;
        this.consumerId = consumerId.toHexString();
        this.tickets = tickets;
        this.payPalId = payPalId;
        this.statusPay = statusPay;
        this.expiredDate = expiredDate;
    }
}
