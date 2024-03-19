package com.example.TicketsService.Mapper;

import com.example.TicketsService.dto.response.ManagerResponse;
import com.example.TicketsService.model.ManagerEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ArrayList;

@Component
public class ManagerMapper {

    public ManagerResponse toResponse(ManagerEntity managerEntity){
        return new ManagerResponse(managerEntity.getIdByString(), managerEntity.getUserId(), managerEntity.getName(), managerEntity.getCheckVat(), managerEntity.getCity(), managerEntity.getCompanyName(), managerEntity.getCompanyStreet(), managerEntity.getNip(), managerEntity.getPhone(), managerEntity.getPostcode(), managerEntity.getRegon());
    }

    public List<ManagerResponse> toResponses(List<ManagerEntity> entityList){
        List<ManagerResponse> responseList = new ArrayList<>();

        for(ManagerEntity entity : entityList){
            ManagerResponse response = toResponse(entity);
            responseList.add(response);
        }
        return responseList;
    }
}