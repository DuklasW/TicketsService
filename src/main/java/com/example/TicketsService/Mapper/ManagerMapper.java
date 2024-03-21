package com.example.TicketsService.Mapper;

import com.example.TicketsService.Mapper.common.AbstractMapper;
import com.example.TicketsService.dto.response.ManagerResponse;
import com.example.TicketsService.model.ManagerEntity;
import org.springframework.stereotype.Component;

@Component
public class ManagerMapper extends AbstractMapper<ManagerEntity, ManagerResponse>{

    @Override
    public ManagerResponse toResponse(ManagerEntity managerEntity){
        return new ManagerResponse(managerEntity.getIdByString(), managerEntity.getUserId(), managerEntity.getName(), managerEntity.getCheckVat(), managerEntity.getCity(), managerEntity.getCompanyName(), managerEntity.getCompanyStreet(), managerEntity.getNip(), managerEntity.getPhone(), managerEntity.getPostcode(), managerEntity.getRegon());
    }
}