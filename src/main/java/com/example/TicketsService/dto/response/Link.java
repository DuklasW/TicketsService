package com.example.TicketsService.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Link {

    private String href;
    private String rel;
    private String method;


    public Link() {

    }

    public Link(String url, String approve, String get) {
        this.href = url;
        this.rel = approve;
        this.method = get;
    }
}
