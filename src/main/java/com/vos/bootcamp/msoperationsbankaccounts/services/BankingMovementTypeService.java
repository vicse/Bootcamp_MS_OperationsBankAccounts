package com.vos.bootcamp.msoperationsbankaccounts.services;

import com.vos.bootcamp.msoperationsbankaccounts.models.BankingMovementType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BankingMovementTypeService {

  public Flux<BankingMovementType> findAll();

  public Mono<BankingMovementType> findById(String id);

  public Mono<BankingMovementType> save(BankingMovementType bankingMovementType);

  public Mono<BankingMovementType> update(String id, BankingMovementType bankingMovementType);

  public Mono<BankingMovementType> deleteById(String id);

}
