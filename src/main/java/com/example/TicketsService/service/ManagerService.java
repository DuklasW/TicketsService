package com.example.TicketsService.service;

import com.example.TicketsService.Factory.ArtistFactory;
import com.example.TicketsService.Mapper.ArtistMapper;
import com.example.TicketsService.Mapper.ManagerMapper;
import com.example.TicketsService.dto.request.CreateArtistRequest;
import com.example.TicketsService.dto.response.ArtistResponse;
import com.example.TicketsService.dto.response.ManagerResponse;
import com.example.TicketsService.model.ArtistEntity;
import com.example.TicketsService.model.ManagerEntity;
import com.example.TicketsService.model.UserEntity;
import com.example.TicketsService.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.bson.types.ObjectId;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ManagerService {

    private final ArtistService artistService;
    private final ManagerRepository managerRepository;
    private final UserService userService;
    private final ManagerMapper managerMapper;
    private final ArtistFactory artistFactory;
    private final ArtistMapper artistMapper;

    @Autowired
    public ManagerService(ArtistService artistService, ManagerRepository managerRepository, UserService userService, ManagerMapper managerMapper, ArtistFactory artistFactory, ArtistMapper artistMapper){
        this.artistService = artistService;
        this.managerRepository = managerRepository;
        this.userService = userService;
        this.managerMapper = managerMapper;
        this.artistFactory = artistFactory;
        this.artistMapper = artistMapper;
    }

    public List<ManagerEntity> getAllManagers(){ return managerRepository.findAll();
    }

    public Optional<ManagerEntity> getManagerByUserId(ObjectId userId){ return managerRepository.findByUserId(userId); }

    public ManagerEntity save(ManagerEntity manager){ return managerRepository.save(manager); }

    public List<ManagerResponse> getAllManagerResponses() {
        List<ManagerEntity> managerEntities = getAllManagers();
        return managerMapper.toResponses(managerEntities);
    }

    public ResponseEntity<List<ArtistResponse>> getArtistsByManagerId(ObjectId managerId) {
        List<ArtistEntity> artists = artistService.getArtistByManagerId(managerId);
        List<ArtistResponse> responseList = artistMapper.toResponses(artists);
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @Transactional
    public ArtistEntity createArtistForManager(String managerEmail, CreateArtistRequest createArtistRequest) {
        UserEntity userEntity = getUserByManagerEmail(managerEmail);
        ManagerEntity managerEntity = getManagerByUser(userEntity);

        if (managerEntityHasArtistWithNickname(managerEntity, createArtistRequest.getNickname())) {
            throw new IllegalArgumentException("Manager already has an artist with the same nickname");
        }

        ArtistEntity artistEntity = artistFactory.createArtist(createArtistRequest, managerEntity);
        return artistService.save(artistEntity);
    }

    private boolean managerEntityHasArtistWithNickname(ManagerEntity managerEntity, String nickname) {
        return artistService.existsByManagerIdAndNickname(managerEntity.getId(), nickname);
    }

    private UserEntity getUserByManagerEmail(String userEmail) {
        return userService.getUserByUserEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Error: don't find user by email from Token"));
    }

    private ManagerEntity getManagerByUser(UserEntity userEntity) {
        return getManagerByUserId(userEntity.getId())
                .orElseThrow(() -> new IllegalArgumentException("Error: manager not found for user"));
    }

    public ResponseEntity<List<ArtistResponse>> getArtistsByUserId(ObjectId userId) {
        ObjectId managerId = getManagerByUserId(userId).orElseThrow().getId();
        return getArtistsByManagerId(managerId);
    }
}
