package com.vos.bootcamp.msoperationsbankaccounts.services;

import com.vos.bootcamp.msoperationsbankaccounts.models.BankingMovementCommission;
import com.vos.bootcamp.msoperationsbankaccounts.repositories.BankingMovementCommissionRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class BankingMovementCommissionsServiceImpl implements BankingMovementCommissionService {

  private final BankingMovementCommissionRepository repository;

  public BankingMovementCommissionsServiceImpl(BankingMovementCommissionRepository repository) {
    this.repository = repository;
  }

  @Override
  public Flux<BankingMovementCommission> findAll() {
    return repository.findAll();
  }

  @Override
  public Mono<BankingMovementCommission> findById(String id) {
    return repository.findById(id);
  }

  @Override
  public Mono<BankingMovementCommission> save(BankingMovementCommission bankingMovementCommission) {
    bankingMovementCommission.setCommissionDate(new Date());
    return repository.save(bankingMovementCommission);
  }

  @Override
  public Mono<BankingMovementCommission> update(String id, BankingMovementCommission bankingMovementCommission) {
    return repository.findById(id)
            .flatMap(bankingMovementCommissionDB -> {

              if (bankingMovementCommission.getAccountNumberOrigin() == null) {
                bankingMovementCommissionDB.setAccountNumberOrigin(bankingMovementCommissionDB.getAccountNumberOrigin());
              } else {
                bankingMovementCommissionDB.setAccountNumberOrigin(bankingMovementCommission.getAccountNumberOrigin());
              }

              if (bankingMovementCommission.getCommissionAmount() == 0) {
                bankingMovementCommissionDB.setCommissionAmount(bankingMovementCommissionDB.getCommissionAmount());
              } else {
                bankingMovementCommissionDB.setAccountNumberOrigin(bankingMovementCommission.getAccountNumberOrigin());
              }

              return repository.save(bankingMovementCommissionDB);

            });
  }

  @Override
  public Mono<BankingMovementCommission> deleteById(String id) {
    return repository.findById(id)
      .flatMap(bankingMovementCommission -> repository.delete(bankingMovementCommission)
        .then(Mono.just(bankingMovementCommission))
      );
  }

  @Override
  public Flux<BankingMovementCommission> findByAccountNumberAndRangeCommissionDate(String accountNumber, Date date, Date date1) {
    return repository.findByCommissionDateIsBetweenAndAccountNumberOrigin(date, date1, accountNumber);
  }

  @Override
  public Flux<BankingMovementCommission> findByRangeCommissionDate(Date date, Date date1) {
    return repository.findByCommissionDateIsBetween(date, date1);
  }
}
