package com.example.TicketsService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "events")
public class EventEntity {

    @Id
    private ObjectId id;
    private Date date;
    private List<ObjectId> artistId;
    private Double price;
    private Integer numberTickets;
    private String location;
    private String city;
    private String postcode;
    private String regon;
    private String street;
    private ObjectId createdBy;
    private String name;
    private String description;
}
