package com.example.TicketsService.Factory;

import com.example.TicketsService.dto.request.CreateArtistRequest;
import com.example.TicketsService.model.ArtistEntity;
import com.example.TicketsService.model.ManagerEntity;

public interface ArtistFactory {

    ArtistEntity createArtist(CreateArtistRequest createArtistRequest, ManagerEntity managerEntity);
}
