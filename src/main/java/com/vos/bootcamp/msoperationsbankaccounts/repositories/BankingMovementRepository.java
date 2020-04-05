package com.vos.bootcamp.msoperationsbankaccounts.repositories;

import com.vos.bootcamp.msoperationsbankaccounts.models.BankingMovement;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.Date;

public interface BankingMovementRepository extends ReactiveMongoRepository<BankingMovement, String> {

  public Flux<BankingMovement> findByMovementDateAndAccountNumberOrigin(Date date, String accountNumber);

}
