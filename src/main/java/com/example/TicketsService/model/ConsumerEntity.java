package com.example.TicketsService.model;

import com.example.TicketsService.model.enums.RegonPolandEnum;
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
@Document(collection = "consumer")
public class ConsumerEntity {

    @Id
    private ObjectId id;
    @NotBlank
    private ObjectId userId;
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    private String city;
    @Pattern(regexp = "^\\[0-9]{9}$", message = "Invalid phone number")
    @NotBlank
    private String phone;
    @Pattern(regexp = "(^\\[0-9]{2}-[0-9]{3})?$", message = "Invalid postcode number")
    private String postcode;
    @NotBlank
    private String regon;
    private String street;
    private String companyCity;
    private String companyName;
    @Pattern(regexp = "d{3}-?\\d{2,3}-?\\d{2,3}-?\\d{2,3}", message = "Invalid nip number")
    private String companyNip;
    private String companyPostcode;
    private String companyStreet;

    public ConsumerEntity(ObjectId userId, String name, String surname, String city, String phone, String postcode, String regon, String street) {
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.city = city;
        this.phone = phone;
        this.postcode = postcode;
        this.regon = regon;
        this.street = street;
    }
}
