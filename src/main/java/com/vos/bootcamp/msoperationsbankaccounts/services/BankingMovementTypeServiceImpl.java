package com.vos.bootcamp.msoperationsbankaccounts.services;

import com.vos.bootcamp.msoperationsbankaccounts.models.BankingMovementType;
import com.vos.bootcamp.msoperationsbankaccounts.repositories.BankingMovementTypeRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BankingMovementTypeServiceImpl implements BankingMovementTypeService {


  private final BankingMovementTypeRepository repository;

  public BankingMovementTypeServiceImpl(BankingMovementTypeRepository repository) {
    this.repository = repository;
  }

  @Override
  public Flux<BankingMovementType> findAll() {
    return repository.findAll();
  }

  @Override
  public Mono<BankingMovementType> findById(String id) {
    return repository.findById(id);
  }

  @Override
  public Mono<BankingMovementType> save(BankingMovementType bankingMovementType) {
    return repository.save(bankingMovementType);
  }

  @Override
  public Mono<BankingMovementType> update(String id, BankingMovementType bankingMovementType) {
    return repository.findById(id)
            .flatMap(bankingMovementTypeDB -> {

              if (bankingMovementType.getName() == null) {
                bankingMovementTypeDB.setName(bankingMovementTypeDB.getName());
              } else {
                bankingMovementTypeDB.setName(bankingMovementType.getName());
              }

              return repository.save(bankingMovementTypeDB);

            });
  }

  @Override
  public Mono<BankingMovementType> deleteById(String id) {
    return repository.findById(id)
            .flatMap(bankingMovementType -> repository.delete(bankingMovementType)
            .then(Mono.just(bankingMovementType)));
  }
}
