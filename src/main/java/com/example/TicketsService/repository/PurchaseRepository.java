package com.example.TicketsService.repository;

import com.example.TicketsService.model.PurchaseEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends MongoRepository<PurchaseEntity, ObjectId> {
}
