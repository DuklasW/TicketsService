package com.example.TicketsService.repository;

import com.example.TicketsService.model.ArtistEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ArtistRepository extends MongoRepository<ArtistEntity, ObjectId> {
    List<ArtistEntity> findByManagerId(ObjectId managerId);

    boolean existsByManagerIdAndNickname(ObjectId idByObjectID, String nickname);

    boolean existsByIdAndManagerId(ObjectId artistId, ObjectId managerId);
}
