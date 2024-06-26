package com.example.TicketsService.repository;

import com.example.TicketsService.model.CommentEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository  extends MongoRepository<CommentEntity, Object> {

    List<CommentEntity> findByArtistId(ObjectId artistId);

    List<CommentEntity> findByConsumerId(ObjectId consumerId);
}