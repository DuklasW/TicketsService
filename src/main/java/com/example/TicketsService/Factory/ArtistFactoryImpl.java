package com.example.TicketsService.Factory;

import com.example.TicketsService.model.ManagerEntity;
import org.springframework.stereotype.Component;
import com.example.TicketsService.model.ArtistEntity;
import com.example.TicketsService.dto.request.CreateArtistRequest;
@Component
public class ArtistFactoryImpl implements ArtistFactory {

    @Override
    public ArtistEntity createArtist(CreateArtistRequest createArtistRequest, ManagerEntity managerEntity) {
        return new ArtistEntity(
                managerEntity.getIdByString(),
                createArtistRequest.getName(),
                createArtistRequest.getSurname(),
                createArtistRequest.getNickname(),
                createArtistRequest.getDescription(),
                true
        );
    }
}
