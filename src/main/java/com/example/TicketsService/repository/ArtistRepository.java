package com.example.TicketsService.repository;

import com.example.TicketsService.model.ArtistEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtistRepository extends MongoRepository<ArtistEntity, ObjectId> {
    List<ArtistEntity> findByManagerId(ObjectId managerId);
}
