package com.vos.bootcamp.msoperationsbankaccounts.repositories;

import com.vos.bootcamp.msoperationsbankaccounts.models.BankAccount;
import reactor.core.publisher.Mono;

public interface BankAccountRepository {

  public Mono<BankAccount> findByAccountNumber(String accountNumber);

  public Mono<Boolean> existsByAccountNumber(String accountNumber);

  public Mono<BankAccount> updateAmount(BankAccount account);

}
