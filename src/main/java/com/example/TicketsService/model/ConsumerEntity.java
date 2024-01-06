package com.example.TicketsService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "consumer")
public class ConsumerEntity {

    @Id
    private ObjectId id;
    private ObjectId userId;
    private String name;
    private String surname;
    private String city;
    private String phone;
    private String postcode;
    private String regon;
    private String street;
    private String companyCity;
    private String companyName;
    private String companyNip;
    private String companyPostcode;
    private String companyStreet;
}
