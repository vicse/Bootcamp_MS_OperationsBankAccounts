package com.vos.bootcamp.msoperationsbankaccounts.repositories;

import com.vos.bootcamp.msoperationsbankaccounts.models.CreditProduct;
import reactor.core.publisher.Mono;

public interface CreditProductRepository {

  public Mono<CreditProduct> findByAccountNumber(String accountNumber);

  public Mono<Boolean> existsByAccountNumber(String accountNumber);

  public Mono<CreditProduct> updateDebtAmount(CreditProduct creditProduct);

}
