package com.example.TicketsService.service;

import com.example.TicketsService.model.ManagerEntity;
import com.example.TicketsService.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.bson.types.ObjectId;
import java.util.List;
import java.util.Optional;

@Service
public class ManagerService {

    @Autowired
    private ManagerRepository managerRepository;

    public List<ManagerEntity> allManagers(){ return managerRepository.findAll();

    }

    public Optional<ManagerEntity> getManagerByUserId(ObjectId userId){ return managerRepository.findByUserId(userId); }

    public ManagerEntity save(ManagerEntity manager){ return managerRepository.save(manager); }
}
