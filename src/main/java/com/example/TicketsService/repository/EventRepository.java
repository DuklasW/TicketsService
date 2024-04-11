package com.example.TicketsService.repository;

import com.example.TicketsService.model.EventEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends MongoRepository<EventEntity, ObjectId> {

    boolean existsByCreatedByManagerAndId(ObjectId createdByManager, ObjectId id);
}
