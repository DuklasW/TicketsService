package com.example.TicketsService.repository;

import com.example.TicketsService.model.UserEntity;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, ObjectId> {

    Optional<UserEntity> findByEmail(String email);

    Boolean existsByEmail(String email);

    void deleteById(@NonNull ObjectId id);

    String getEmailById(ObjectId id);
}
