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
@Document(collection = "manager")
public class ManagerEntity {

    @Id
    private ObjectId id;
    private ObjectId userId;
    private Boolean checkVat;
    private String city;
    private String companyName;
    private String companyStreet;
    private String nip;
    private String phone;
    private String postalcode;
    private String regon;
}
