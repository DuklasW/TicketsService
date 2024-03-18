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

    public Link(String href, String rel, String method) {
        this.href = href;
        this.rel = rel;
        this.method = method;
    }
}
