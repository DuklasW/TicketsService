package com.example.TicketsService.service;

import com.example.TicketsService.model.EventEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderPreparationService {


    public String generateOrderPayPallData(EventEntity event, int quantity){
        ObjectMapper mapper = new ObjectMapper();

        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        String currency = "PLN";

        Map<String, Object> request = new HashMap<>();
        request.put("intent", "CAPTURE");

        List<Map<String, Object>> purchaseUnits = new ArrayList<>();
        Map<String, Object> purchaseUnit = new HashMap<>();

        List<Map<String, Object>> items = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        item.put("name", event.getName());
        item.put("description", event.getDescription());
        item.put("quantity", quantity);

        Map<String, Object> unitAmount = new HashMap<>();
        unitAmount.put("currency_code", currency);
        unitAmount.put("value", event.getPrice());
        item.put("unit_amount", unitAmount);

        items.add(item);

        purchaseUnit.put("items", items);

        Map<String, Object> amount = new HashMap<>();
        amount.put("currency_code", currency);
        amount.put("value", quantity*event.getPrice());

        Map<String, Object> breakdown = new HashMap<>();
        Map<String, Object> itemTotal = new HashMap<>();
        itemTotal.put("currency_code", currency);
        itemTotal.put("value", quantity*event.getPrice());
        breakdown.put("item_total", itemTotal);
        amount.put("breakdown", breakdown);

        purchaseUnit.put("amount", amount);

        purchaseUnits.add(purchaseUnit);
        request.put("purchase_units", purchaseUnits);

        Map<String, Object> applicationContext = new HashMap<>();
        applicationContext.put("return_url", "https://node.codedream.eu");
        applicationContext.put("cancel_url", "https://node.codedream.eu/cancel");
        request.put("application_context", applicationContext);

        try{
            return mapper.writeValueAsString(request);
        }catch (JsonProcessingException e){
            throw new RuntimeException("Error creating JSON payment data: ", e);
        }
    }
}
