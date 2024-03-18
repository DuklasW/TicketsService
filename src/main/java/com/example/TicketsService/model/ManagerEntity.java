package com.example.TicketsService.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    private String id;
    @NotBlank
    private ObjectId userId;
    @NotBlank
    private String name;
    @NotBlank
    private Boolean checkVat;
    @NotBlank
    private String city;
    @NotBlank
    private String companyName;
    @NotBlank
    private String companyStreet;
    @NotBlank
    @Pattern(regexp = "d{3}-?\\d{2,3}-?\\d{2,3}-?\\d{2,3}", message = "Invalid nip number")
    private String nip;
    @NotBlank
    @Pattern(regexp = "^\\[0-9]{9}$", message = "Invalid phone number")
    private String phone;
    @NotBlank
    @Pattern(regexp = "^\\[0-9]{2}-[0-9]{3}$", message = "Invalid postcode number")
    private String postcode;
    @NotBlank
    private String regon;

    public ManagerEntity(ObjectId id, ObjectId userId, String name, Boolean checkVat, String city, String companyName, String companyStreet, String nip, String phone, String postcode, String regon) {
        this.id = id.toHexString();
        this.userId = userId;
        this.name = name;
        this.checkVat = checkVat;
        this.city = city;
        this.companyName = companyName;
        this.companyStreet = companyStreet;
        this.nip = nip;
        this.phone = phone;
        this.postcode = postcode;
        this.regon = regon;
    }

    public ManagerEntity(ObjectId userId, String name, Boolean checkVat, String city, String companyName, String companyStreet, String nip, String phone, String postcode, String regon) {
        this.userId = userId;
        this.name = name;
        this.checkVat = checkVat;
        this.city = city;
        this.companyName = companyName;
        this.companyStreet = companyStreet;
        this.nip = nip;
        this.phone = phone;
        this.postcode = postcode;
        this.regon = regon;
    }

    public ObjectId getIdByObjectID(){
        return new ObjectId(this.id);
    }

}
