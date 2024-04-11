package com.example.TicketsService.controller;

import com.example.TicketsService.dto.request.PurchaseAcceptRequest;
import com.example.TicketsService.dto.request.PurchaseTicketRequest;
import com.example.TicketsService.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@Tag(name="Purchase Controller", description = "Kontroler służący do zadządzania płatnościami")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final PayPallService payPallService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService, PayPallService payPallService) {
        this.purchaseService = purchaseService;
        this.payPallService = payPallService;
    }
    @Operation(summary = "Kup bilet poprzez PayPall", description = "Pozwala kupić bilety na wybrane wydarzenie korzystając z płatności PayPall sandbox. " +
            "Endpoint dostępny dla zalogowanych użytkowników, z rolą 'CONSUMER'. " +
            "Nie można kupić biletu na wydarzenie, które już się odbyło, np. 65b590e4aa11c12eb2294ba8.")
    @PreAuthorize("hasRole('ROLE_CONSUMER')")
    @PostMapping("/makePayment")
    public Mono<ResponseEntity<String>> makePayment(@Valid @RequestBody PurchaseTicketRequest request){
        return purchaseService.makePayment(request);

    }

    @Operation(summary = "Akceptacja płatności", description = "Endpoint demonstracyjny (niezabezpieczony), wymagany do zaakceptowania płatności po zakupie biletu.")
    @PostMapping("acceptPayment")
    public Mono<ResponseEntity<String>> acceptPayment(@Valid @RequestBody PurchaseAcceptRequest acceptRequest){
        return purchaseService.acceptPayment(acceptRequest);
    }

    @Operation(summary = "Spradzenie tokeny dostępu", description = "Endpoint tylko dla administracji, który zwraca aktualny token dostępu do API PayPall")
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping("/testGetBerrerToken")
    public Mono<ResponseEntity<String>> getBererToken(){
        return payPallService.getAccessToken()
                .map(tokenResponse -> ResponseEntity.ok(tokenResponse.getAccess_token()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Pobierz szczegóły zamówienia o id", description = "Endpoint demonstracyjny (niezabezpieczony) służący do pobrania podstawowych informacji o zamówieniu po podaniu jego identyfikatora.")
            @GetMapping("/{payPallId}")
    public ResponseEntity<?> getPaymentByPayPallId(@PathVariable String payPallId) {
        return purchaseService.getPaymentById(payPallId);
    }
}
