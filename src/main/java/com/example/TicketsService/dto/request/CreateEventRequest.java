package com.example.TicketsService.dto.request;

import com.example.TicketsService.model.enums.RegonPolandEnum;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class CreateEventRequest {

    private List<Date> dates;
    private List<String> artistName;
    @NotNull
    @DecimalMin(value = "0.0", message = "Price must be greater than or equal to 0 ")
    private Double price;
    @NotNull
    @DecimalMin(value = "0", inclusive = false, message = "Number of tickets must be greater than 0")
    private Integer numberTickets;
    @NotBlank
    private String location;
    @NotBlank
    private String city;
    @NotBlank
    @Pattern(regexp = "^[0-9]{2}-[0-9]{3}$", message = "Invalid postcode number")
    private String postcode;
    @NotBlank
    private String regon;
    @NotBlank
    private String street;
    @NotBlank
    private String createdBy;
    @NotBlank
    private String name;
    @NotBlank
    private String description;

    public boolean isValidRegon(){
        return RegonPolandEnum.formDisplayRegon(regon) != null;
    }

    public ObjectId getCreatedByByObjectId(){
        return new ObjectId(this.createdBy);
    }

}
