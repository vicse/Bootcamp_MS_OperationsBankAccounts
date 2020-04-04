package com.vos.bootcamp.msoperationsbankaccounts.repositories;

import com.vos.bootcamp.msoperationsbankaccounts.models.BankingMovementCommission;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BankingMovementCommissionRepository extends ReactiveMongoRepository<BankingMovementCommission, String> {
}
