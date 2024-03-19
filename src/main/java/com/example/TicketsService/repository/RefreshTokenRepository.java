package com.example.TicketsService.repository;

import com.example.TicketsService.model.RefreshTokenEntity;
import com.example.TicketsService.model.UserEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshTokenEntity, ObjectId> {
    Optional<RefreshTokenEntity> findByToken(String token);

    Integer deleteByUserEntity(UserEntity userEntity);
}
