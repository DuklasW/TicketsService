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
    private ObjectId id;
    private ObjectId eventId;
    private Date date;
    private ObjectId consumerId;
    private Integer tickets;
    private String sattusPay;
}
