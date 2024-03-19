package com.example.TicketsService.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Link {

    private String href;
    private String rel;
    private String method;
}
