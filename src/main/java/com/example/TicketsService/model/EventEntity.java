package com.example.TicketsService.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "events")
public class EventEntity {
//TODO ZMIENIAM STRING na OBJECTID
    @Id
    private ObjectId id;
    @NotBlank
    private Date date;
    private List<String> artistName;
    @NotNull
    private Double price;
    @NotNull
    private Integer ticketsNumber;
    @NotNull
    private Integer ticketsBought;
    @NotBlank
    private String location;
    @NotBlank
    private String city;
    @NotBlank
    @Pattern(regexp = "^\\[0-9]{2}-[0-9]{3}$", message = "Invalid postcode number")
    private String postcode;
    @NotBlank
    private String regon;
    @NotBlank
    private String street;
    @NotBlank
    @Field(targetType = FieldType.OBJECT_ID)
    private String createdBy;
    @NotBlank
    private String name;
    @NotBlank
    private String description;



    public EventEntity(Date date, List<String> artistId, Double price, Integer ticketsNumber, Integer ticketsBought, String location, String city, String postcode, String regon, String street, String createdBy, String name, String description) {
        this.date = date;
        this.artistName = artistId;
        this.price = price;
        this.ticketsNumber = ticketsNumber;
        this.ticketsBought = ticketsBought;
        this.location = location;
        this.city = city;
        this.postcode = postcode;
        this.regon = regon;
        this.street = street;
        this.createdBy = createdBy;
        this.name = name;
        this.description = description;
    }
}
