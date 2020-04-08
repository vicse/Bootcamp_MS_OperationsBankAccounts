package com.vos.bootcamp.msoperationsbankaccounts.repositories;

import com.vos.bootcamp.msoperationsbankaccounts.models.BankingMovementCommission;
import java.util.Date;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;


public interface BankingMovementCommissionRepository
        extends ReactiveMongoRepository<BankingMovementCommission, String> {

  public Flux<BankingMovementCommission> findByCommissionDateIsBetweenAndAccountNumberOrigin(
          Date date, Date date2, String accountNumber);

  public Flux<BankingMovementCommission> findByCommissionDateIsBetween(Date date, Date date2);

}
