package com.example.TicketsService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Kontroler służący do testowania aktualnych uprawnień użytkowników. Dostępne role: 'CONSUMER', 'MANAGER', 'MODERATOR' i 'ADMIN'.
 * Sprawdzanie ról odbywa się poprzez analizę tokenu JWT przekazanego w nagłówku 'Authorization'.
 */
@Tag(name="Auth Test Controller", description = "Kontroler służący do testowania aktualnych uprawnień użytkowników. Dostępne role: 'CONSUMER', 'MANAGER', 'MODERATOR' i 'ADMIN'")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/test")
public class AuthTestController {

    @GetMapping("/all")
    @Operation(summary = "Publiczny endpoint.", description = "Publiczny endpoint dostępny dla wszystkich użytkowników.")
    public ResponseEntity<String> allAccess() {
        return ResponseEntity.ok("Public Content.");
    }

    @GetMapping("/consumer")
    @PreAuthorize("hasRole('ROLE_CONSUMER') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @Operation(summary = "Endpoint dla użytkowników z rolami 'CONSUMER', 'MODERATOR' lub 'ADMIN'.", description = "Endpoint dostępny dla zalogowanych użytkowników, z rolą minimum 'CONSUMER'")
    public ResponseEntity<String> consumerAccess() {
        return ResponseEntity.ok("Consumer Content.");
    }

    @GetMapping("/manager")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @Operation(summary = "Endpoint dla użytkowników z rolami 'MANAGER', 'MODERATOR' lub 'ADMIN'.", description = "Endpoint dostępny dla zalogowanych użytkowników, z rolą minimum 'MANAGER'")
    public ResponseEntity<String> managerAccess() {
        return ResponseEntity.ok("Manager Content");
    }

    @GetMapping("/moderator")
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @Operation(summary = "Endpoint tylko dla 'MODERATOR' i 'ADMIN'.", description = "Endpoint dostępny dla administracji, z rolą minimum 'MODERATOR'")
    public ResponseEntity<String> moderatorAccess() {
        return ResponseEntity.ok("Moderator Content.");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Endpoint tylko dla użytkowników z rolą 'ADMIN'.", description = "Jest to endpoint dostępny dla użytkowników z rolą 'ADMIN'")
    public ResponseEntity<String> adminAccess() {
        return ResponseEntity.ok("Admin Content.");
    }
}
