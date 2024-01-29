package com.example.TicketsService.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class AuthTestController {
    @GetMapping("/all")
    public String allAccess(){
        return "Public Content.";
    }

    @GetMapping("/consumer")
    @PreAuthorize("hasRole('ROLE_CONSUMER') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    public String consumerAccess(){
        return "Consumer Content.";
    }

    @GetMapping("/manager")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    public String managerAccess(){return "Manager Content"; }

    @GetMapping("/moderator")
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    public String moderatorAccess(){
        return "Moderator Content.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminAccess(){
        return "Admin Content.";
    }
}
