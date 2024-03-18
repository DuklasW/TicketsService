package com.example.TicketsService.service;

import com.example.TicketsService.dto.request.CreateArtistRequest;
import com.example.TicketsService.model.ArtistEntity;
import com.example.TicketsService.model.ManagerEntity;
import com.example.TicketsService.model.UserEntity;
import com.example.TicketsService.repository.ArtistRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ArtistService{


    @Autowired
    private ArtistRepository artistRepository;

    public List<ArtistEntity> getArtistByManagerId(ObjectId managerId){
        return artistRepository.findByManagerId(managerId);
    }

    public Optional<ArtistEntity> getArtistByArtistId(ObjectId artistId){return artistRepository.findById(artistId); }

    @Transactional
    public ArtistEntity save(ArtistEntity arist){ return artistRepository.save(arist); }

    public boolean existsByManagerIdAndNickname(ObjectId idByObjectID, String nickname) {
        return artistRepository.existsByManagerIdAndNickname(idByObjectID, nickname);
    }
}
