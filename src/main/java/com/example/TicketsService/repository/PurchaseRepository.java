package com.example.TicketsService.repository;

import com.example.TicketsService.model.PurchaseEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends MongoRepository<PurchaseEntity, ObjectId> {

    PurchaseEntity findByPayPalId(String paypalId);

    List<PurchaseEntity> findByConsumerId(ObjectId consumerId);
}
