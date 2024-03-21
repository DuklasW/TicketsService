package com.example.TicketsService.service;

import com.example.TicketsService.Mapper.ArtistMapper;
import com.example.TicketsService.dto.response.ArtistResponse;
import com.example.TicketsService.model.ArtistEntity;
import com.example.TicketsService.repository.ArtistRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ArtistService{


    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;

    @Autowired
    public ArtistService(ArtistRepository artistRepository, ArtistMapper artistMapper) {
        this.artistRepository = artistRepository;
        this.artistMapper = artistMapper;
    }

    public List<ArtistEntity> getArtistByManagerId(ObjectId managerId){
        return artistRepository.findByManagerId(managerId);
    }

    public ArtistResponse getArtistByArtistId(ObjectId artistId){
        ArtistEntity artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new IllegalArgumentException("Artist not found for id: " + artistId.toHexString()));
        return artistMapper.mapToResponse(artist);
    }

    @Transactional
    public ArtistEntity save(ArtistEntity arist){ return artistRepository.save(arist); }

    public boolean existsByManagerIdAndNickname(ObjectId idByObjectID, String nickname) {
        return artistRepository.existsByManagerIdAndNickname(idByObjectID, nickname);
    }
}
