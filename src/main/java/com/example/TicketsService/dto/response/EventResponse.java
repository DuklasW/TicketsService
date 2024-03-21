package com.example.TicketsService.dto.response;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class EventResponse {
    private String id;
    private Date date;
    private List<String> artistName;
    private Double price;
    private Integer ticketsNumber;
    private Integer ticketsBought;
    private String location;
    private String city;
    private String postcode;
    private String regon;
    private String street;
    private String createdBy;
    private String name;
    private String description;
}
