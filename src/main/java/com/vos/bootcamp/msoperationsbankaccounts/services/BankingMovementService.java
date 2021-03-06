package com.vos.bootcamp.msoperationsbankaccounts.services;

import com.vos.bootcamp.msoperationsbankaccounts.models.BankAccount;
import com.vos.bootcamp.msoperationsbankaccounts.models.BankingMovement;
import com.vos.bootcamp.msoperationsbankaccounts.models.CreditProduct;
import com.vos.bootcamp.msoperationsbankaccounts.utils.ICrud;
import java.util.Date;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BankingMovementService extends ICrud<BankingMovement> {

  public Flux<BankingMovement> findByAccountNumberAndMovementDate(String accountNumber, Date date,Date date1);

  public Flux<BankingMovement> findByNumDocOwner(String numDocOwner);

  public Mono<BankingMovement> deposit(BankingMovement bankingMovement);

  public Mono<BankingMovement> withdraw(BankingMovement bankingMovement);

  public Mono<BankingMovement> creditProductPayment(BankingMovement bankingMovement, String numAccountProductCredit);

  public Mono<Boolean> validateBankAccount(String accountNumber, String numDocOwner);

  public Mono<BankAccount> updateBankAccountAmount(BankAccount bankAccount);

  public Mono<BankAccount> findBankAccountByAccountNumber(String accountNumber);

  public Mono<CreditProduct> updateCreditProductDebtAmount(CreditProduct creditProduct);

  public Mono<Boolean> chargeCommission(String accountNumber, Date date);



}
