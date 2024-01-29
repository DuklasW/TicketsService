package com.example.TicketsService.service;

import com.example.TicketsService.model.ArtistEntity;
import com.example.TicketsService.model.ManagerEntity;
import com.example.TicketsService.repository.ArtistRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArtistService{

    @Autowired
    private ArtistRepository artistRepository;

    public List<ArtistEntity> getArtistByManagerId(ObjectId managerId){
        return artistRepository.findByManagerId(managerId);
    }

    public ArtistEntity save(ArtistEntity arist){ return artistRepository.save(arist); }
}
