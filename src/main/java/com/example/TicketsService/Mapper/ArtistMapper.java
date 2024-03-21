package com.example.TicketsService.Mapper;

import com.example.TicketsService.Mapper.common.AbstractMapper;
import com.example.TicketsService.dto.response.ArtistResponse;
import com.example.TicketsService.model.ArtistEntity;
import org.springframework.stereotype.Component;

@Component
public class ArtistMapper extends AbstractMapper<ArtistEntity, ArtistResponse> {

    @Override
    public ArtistResponse toResponse(ArtistEntity artistEntity) {
        ArtistResponse artistResponse = new ArtistResponse();

        artistResponse.setId(artistEntity.getId().toHexString());
        artistResponse.setManagerId(artistEntity.getManagerId());
        artistResponse.setName(artistEntity.getName());
        artistResponse.setSurname(artistEntity.getSurname());
        artistResponse.setNickname(artistEntity.getNickname());
        artistResponse.setDescription(artistEntity.getDescription());
        artistResponse.setIsActive(artistEntity.getIsActive());

        return artistResponse;
    }
}
