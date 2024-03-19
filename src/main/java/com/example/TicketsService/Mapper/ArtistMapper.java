package com.example.TicketsService.Mapper;

import com.example.TicketsService.dto.response.ArtistResponse;
import com.example.TicketsService.model.ArtistEntity;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class ArtistMapper {

    public List<ArtistResponse> mapToResponses(List<ArtistEntity> entities) {
        List<ArtistResponse> responses = new ArrayList<>();

        for(ArtistEntity artistEntity : entities){
            responses.add(mapToResponse(artistEntity));
        }
        return responses;
    }

    private ArtistResponse mapToResponse(ArtistEntity artistEntity) {
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
