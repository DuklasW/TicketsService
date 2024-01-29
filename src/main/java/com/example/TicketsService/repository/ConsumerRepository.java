package com.example.TicketsService.repository;

import com.example.TicketsService.model.ConsumerEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConsumerRepository extends MongoRepository<ConsumerEntity, ObjectId> {

    ConsumerEntity findConsumerEntityByUserId(ObjectId userId);

}
