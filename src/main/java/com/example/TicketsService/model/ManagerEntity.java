package com.example.TicketsService.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "manager")
public class ManagerEntity {

    //TODO zmieniłem String na ObjectId, zakomentowałem funckje na dole moraz wManagerService.java managerEntityHasArtistWithNickname, zmieniłem na getId
    @Id
    private ObjectId id;
    @NotBlank
    @Field(targetType = FieldType.OBJECT_ID)
    private String userId;
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


    public ManagerEntity(String userId, String name, Boolean checkVat, String city, String companyName, String companyStreet, String nip, String phone, String postcode, String regon) {
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

    @JsonIgnore
    public String getIdByString() {
        return id.toHexString();
    }
}