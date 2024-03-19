package com.example.TicketsService.service;

import com.example.TicketsService.dto.response.PayPallAcceptPaymentReponse;
import com.example.TicketsService.dto.response.PayPallMakePaymentResponse;
import com.example.TicketsService.dto.response.PayPallTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.Base64;

@Service
public class PayPallService {

    private final WebClient webClient;

    @Value("${TicketService.app.paypallId}")
    private String clientId;

    @Value("${TicketService.app.paypallSecret}")
    private String clientSecret;

    @Autowired
    public PayPallService(WebClient webClient) {
        this.webClient = webClient;
    }

    //Funkcja która zwraca access token z api PayPall
    public Mono<PayPallTokenResponse>  getAccessToken(){
        String url = "/v1/oauth2/token";
        String grantType = "client_credentials";

        String creadentials = String.format("%s:%s", clientId, clientSecret);
        String encodedCredentials = Base64.getEncoder().encodeToString(creadentials.getBytes());


        return webClient.post()
                .uri(url)
                .headers(headers -> headers.setBasicAuth(encodedCredentials))
                .body(BodyInserters.fromFormData("grant_type", grantType))
                .retrieve()
                .bodyToMono(PayPallTokenResponse.class);
    }

    //Funkcja tworząca płatność w api PayPall
    public Mono<PayPallMakePaymentResponse> makePayment(String berer, String jsonBodyPayment){
        String url = "/v2/checkout/orders";
        return webClient.post()
                .uri(url)
                .headers(headers -> {
                    headers.add("Prefer", "return=minimal");
                    headers.setBearerAuth(berer);
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .body(BodyInserters.fromValue(jsonBodyPayment))
                .retrieve()
                .bodyToMono(PayPallMakePaymentResponse.class);
    }

    //Funkcja akceptująca płatność w api PayPall
    public Mono<PayPallAcceptPaymentReponse> acceptPayment(String berer, String orderId){
        String url = "/v2/checkout/orders/" + orderId + "/capture";
        return webClient.post()
                .uri(url)
                .headers(headers ->{
                    headers.setBearerAuth(berer);
                    headers.add("Prefer", "return=minimal");
                    headers.add("Access-Control-Allow-Origin", "*");
                    headers.setContentLength(0);
                })
                .body(BodyInserters.empty())
                .retrieve()
                .bodyToMono(PayPallAcceptPaymentReponse.class);
    }
}
