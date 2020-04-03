package com.vos.bootcamp.msoperationsbankaccounts.repositories;

import com.vos.bootcamp.msoperationsbankaccounts.models.BankingMovement;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BankingMovementRepository extends ReactiveMongoRepository<BankingMovement, String> {
}
