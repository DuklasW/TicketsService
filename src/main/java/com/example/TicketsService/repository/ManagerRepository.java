package com.example.TicketsService.repository;

import com.example.TicketsService.model.ManagerEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepository extends MongoRepository<ManagerEntity, ObjectId> {
    Optional<ManagerEntity> findByUserId(ObjectId userId);

}
