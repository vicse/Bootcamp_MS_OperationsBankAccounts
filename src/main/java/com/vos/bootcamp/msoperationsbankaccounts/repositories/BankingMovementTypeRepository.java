package com.vos.bootcamp.msoperationsbankaccounts.repositories;

import com.vos.bootcamp.msoperationsbankaccounts.models.BankingMovementType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BankingMovementTypeRepository extends ReactiveMongoRepository<BankingMovementType, String> {
}
