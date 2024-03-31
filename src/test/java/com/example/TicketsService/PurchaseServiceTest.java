package com.example.TicketsService;

import com.example.TicketsService.Factory.PurchaseFactory;
import com.example.TicketsService.model.EventEntity;
import com.example.TicketsService.model.PurchaseEntity;
import com.example.TicketsService.dto.request.PurchaseTicketRequest;
import com.example.TicketsService.dto.response.PayPallMakePaymentResponse;
import com.example.TicketsService.repository.PurchaseRepository;
import com.example.TicketsService.security.service.UserDetailsImpl;
import com.example.TicketsService.service.*;
import com.example.TicketsService.validate.PayPalValidator;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.example.TicketsService.dto.response.Link;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import reactor.core.publisher.Mono;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PurchaseServiceTest {

    @Mock
    private ConsumerService consumerService;
    @Mock
    private EventService eventService;
    @Mock
    private OrderPreparationService orderPreparationService;
    @Mock
    private PayPallService payPallService;
    @Mock
    private TokenCacheService tokenCacheService;
    @Mock
    private PurchaseRepository purchaseRepository;
    @Mock
    private PayPalValidator payPalValidator;
    @Mock
    private PurchaseFactory purchaseFactory;

    @InjectMocks
    private PurchaseService purchaseService;
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp(){
        userDetails = createUserDetails();
        mockSecurityContext(userDetails);
    }

    @Test
    void makePayment_ValidRequest_ReturnsOkResponse() {
        // Arrange
        ObjectId eventId = new ObjectId();
        PurchaseTicketRequest request = new PurchaseTicketRequest();
        request.setEventId(eventId.toHexString());
        request.setTickets(2);

        ObjectId consumerId = userDetails.getId();
        EventEntity event = new EventEntity();
        event.setTicketsBought(0);
        String jsonBodyPayment = "jsonBodyPayment";
        String bererToken = "bererToken";
        PayPallMakePaymentResponse payPallResponse = createPayPallResponse();
        PurchaseEntity purchaseEntity = new PurchaseEntity();


        when(consumerService.getConsumerIdByEmail(userDetails.getEmail())).thenReturn(consumerId);
        when(eventService.getEventEntityByEventId(eventId)).thenReturn(Optional.of(event));
        when(orderPreparationService.generateOrderPayPallData(event, 2)).thenReturn(jsonBodyPayment);
        when(tokenCacheService.getToken()).thenReturn(bererToken);
        when(payPallService.makePayment(bererToken, jsonBodyPayment)).thenReturn(Mono.just(payPallResponse));
        when(purchaseFactory.createPurchase(eq(request), any(Date.class), eq(consumerId), eq(payPallResponse))).thenReturn(purchaseEntity);


        // Act & Assert
        Assertions.assertDoesNotThrow(() -> {
            ResponseEntity<String> response = purchaseService.makePayment(request).block();
            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
            Assertions.assertTrue(response.getBody().contains("Payment created, id: paymentId"));
        });

        verify(eventService).save(event);
        verify(purchaseRepository).save(purchaseEntity);
    }

    private PayPallMakePaymentResponse createPayPallResponse() {
        PayPallMakePaymentResponse payPallResponse = new PayPallMakePaymentResponse();
        payPallResponse.setId("paymentId");
        payPallResponse.setStatus("CREATED");

        List<Link> links = new ArrayList<>();
        links.add(new Link("https://api.sandbox.paypal.com/v2/checkout/orders/paymentId", "self", "GET"));
        links.add(new Link("https://www.sandbox.paypal.com/checkoutnow?token=paymentId", "approve", "GET"));
        links.add(new Link("https://api.sandbox.paypal.com/v2/checkout/orders/paymentId", "update", "PATCH"));
        links.add(new Link("https://api.sandbox.paypal.com/v2/checkout/orders/paymentId/capture", "capture", "POST"));
        payPallResponse.setLinks(links);
        return payPallResponse;
    }

    @Test
    void getEvent_ValidEventId_ReturnsEvent() {
        // Arrange
        ObjectId eventId = new ObjectId();
        EventEntity event = new EventEntity();
        when(eventService.getEventEntityByEventId(eventId)).thenReturn(Optional.of(event));

        // Act
        EventEntity result = purchaseService.getEvent(eventId.toHexString());

        // Assert
        Assertions.assertEquals(event, result);
    }

    @Test
    void getEvent_InvalidEventId_ThrowsException() {
        // Arrange
        ObjectId eventId = new ObjectId();
        when(eventService.getEventEntityByEventId(eventId)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(RuntimeException.class, () -> purchaseService.getEvent(eventId.toHexString()));
    }

    @Test
    void getConsumerId_ValidUserEmail_ReturnsConsumerId() {
        // Arrange
        when(consumerService.getConsumerIdByEmail(userDetails.getEmail())).thenReturn(userDetails.getId());

        // Act
        ObjectId result = purchaseService.getConsumerId(userDetails.getEmail());

        // Assert
        Assertions.assertEquals(userDetails.getId(), result);
    }

    private UserDetailsImpl createUserDetails() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
        return new UserDetailsImpl(new ObjectId("111111111111111111111111"), "john.doe@example.com", "password", authorities);
    }

    private void mockSecurityContext(UserDetailsImpl userDetails) {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.setContext(securityContext);
    }
}