package com.example.TicketsService.service;

import com.example.TicketsService.dto.request.CreateArtistRequest;
import com.example.TicketsService.dto.response.ArtistResponse;
import com.example.TicketsService.dto.response.ManagerResponse;
import com.example.TicketsService.model.ArtistEntity;
import com.example.TicketsService.model.ManagerEntity;
import com.example.TicketsService.model.UserEntity;
import com.example.TicketsService.repository.ManagerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.bson.types.ObjectId;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ManagerService {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private UserService userService;

    public List<ManagerEntity> allManagers(){ return managerRepository.findAll();
    }

    public Optional<ManagerEntity> getManagerByUserId(ObjectId userId){ return managerRepository.findByUserId(userId); }

    public ManagerEntity save(ManagerEntity manager){ return managerRepository.save(manager); }

    public List<ManagerResponse> getAllManagerResponses() {
        List<ManagerEntity> managerEntities = managerRepository.findAll();
        List<ManagerResponse> managerResponses = new ArrayList<>();
        for(ManagerEntity managerEntity : managerEntities){
            managerResponses.add(new ManagerResponse(managerEntity.getId(), managerEntity.getUserId(), managerEntity.getName(), managerEntity.getCheckVat(), managerEntity.getCity(), managerEntity.getCompanyName(), managerEntity.getCompanyStreet(), managerEntity.getNip(), managerEntity.getPhone(), managerEntity.getPostcode(), managerEntity.getRegon()));
        }
        return managerResponses;
    }

    public ResponseEntity<List<ArtistResponse>> getArtistsByManagerId(ObjectId managerId) {
        List<ArtistEntity> artists = artistService.getArtistByManagerId(managerId);
        List<ArtistResponse> responseList = new ArrayList<>();
        for(ArtistEntity artistEntity : artists){
            ArtistResponse artistResponse = new ArtistResponse();
            BeanUtils.copyProperties(artistEntity, artistResponse);
            artistResponse.setId(artistEntity.getId().toHexString());
            artistResponse.setManagerId(artistEntity.getManagerId());
            responseList.add(artistResponse);
        }
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @Transactional
    public ArtistEntity createArtistForManager(String managerEmail, CreateArtistRequest createArtistRequest) {
        UserEntity userEntity = getUserByManagerEmail(managerEmail);
        ManagerEntity managerEntity = getManagerByUser(userEntity);

        if (managerEntityHasArtistWithNickname(managerEntity, createArtistRequest.getNickname())) {
            throw new IllegalArgumentException("Manager already has an artist with the same nickname");
        }

        ArtistEntity artistEntity = mapToArtistEntity(managerEntity, createArtistRequest);
        return artistService.save(artistEntity);
    }

    private boolean managerEntityHasArtistWithNickname(ManagerEntity managerEntity, String nickname) {
        return artistService.existsByManagerIdAndNickname(managerEntity.getIdByObjectID(), nickname);
    }

    private UserEntity getUserByManagerEmail(String userEmail) {
        return userService.getUserByUserEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Error: don't find user by email from Token"));
    }

    private ManagerEntity getManagerByUser(UserEntity userEntity) {
        return getManagerByUserId(userEntity.getIdAsObjectId())
                .orElseThrow(() -> new IllegalArgumentException("Error: manager not found for user"));
    }

    private ArtistEntity mapToArtistEntity(ManagerEntity managerEntity, CreateArtistRequest createArtistRequest) {
        return new ArtistEntity(
                managerEntity.getId(),
                createArtistRequest.getName(),
                createArtistRequest.getSurname(),
                createArtistRequest.getNickname(),
                createArtistRequest.getDescription(),
                true
        );
    }
}
