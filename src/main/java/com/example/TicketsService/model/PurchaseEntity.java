package com.example.TicketsService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "purchases")
public class PurchaseEntity {

    @Id
    private String id;
    private String eventId;
    private Date date;
    private String consumerId;
    private Integer tickets;
    private String statusPay;

    public PurchaseEntity(ObjectId id, ObjectId eventId, Date date, ObjectId consumerId, Integer tickets, String statusPay) {
        this.id = id.toHexString();
        this.eventId = eventId.toHexString();
        this.date = date;
        this.consumerId = consumerId.toHexString();
        this.tickets = tickets;
        this.statusPay = statusPay;
    }
    public PurchaseEntity(ObjectId eventId, Date date, ObjectId consumerId, Integer tickets, String statusPay) {
        this.eventId = eventId.toHexString();
        this.date = date;
        this.consumerId = consumerId.toHexString();
        this.tickets = tickets;
        this.statusPay = statusPay;
    }


    public ObjectId getIdByObjectId(){
        return new ObjectId(this.id);
    }
    public ObjectId getEventIdByObjectId(){
        return new ObjectId(this.id);
    }
    public ObjectId getConsumerIdByObjectId(){
        return new ObjectId(this.id);
    }
}
