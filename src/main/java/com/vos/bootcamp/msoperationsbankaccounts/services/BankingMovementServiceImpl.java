package com.vos.bootcamp.msoperationsbankaccounts.services;

import com.vos.bootcamp.msoperationsbankaccounts.dto.PaymentCreditProductDTO;
import com.vos.bootcamp.msoperationsbankaccounts.models.BankAccount;
import com.vos.bootcamp.msoperationsbankaccounts.models.BankingMovement;
import com.vos.bootcamp.msoperationsbankaccounts.models.CreditProduct;
import com.vos.bootcamp.msoperationsbankaccounts.repositories.BankAccountRepository;
import com.vos.bootcamp.msoperationsbankaccounts.repositories.BankingMovementRepository;
import java.util.Date;

import com.vos.bootcamp.msoperationsbankaccounts.repositories.CreditProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class BankingMovementServiceImpl implements BankingMovementService {

  private final BankingMovementRepository movementRepository;
  private final BankAccountRepository bankAccountRepository;
  private final CreditProductRepository creditProductRepository;

  public BankingMovementServiceImpl(BankingMovementRepository movementRepository, BankAccountRepository bankAccountRepository, CreditProductRepository creditProductRepository) {
    this.movementRepository = movementRepository;
    this.bankAccountRepository = bankAccountRepository;
    this.creditProductRepository = creditProductRepository;
  }

  @Override
  public Flux<BankingMovement> findAll() {
    return movementRepository.findAll();
  }

  @Override
  public Flux<BankingMovement> findByAccountNumberAndMovementDate(String accountNumber, Date date) {
    return movementRepository.findByMovementDateAndAccountNumber(date, accountNumber);
  }

  @Override
  public Flux<BankingMovement> findByNumDocOwner(String numDocOwner) {
    return movementRepository.findByNumDocOwner(numDocOwner);
  }

  @Override
  public Mono<Boolean> validateBankAccount(String accountNumber, String numDocOwner) {

    Mono<BankAccount> bankAccountMono = bankAccountRepository.findByAccountNumber(accountNumber);

    Mono<Boolean> existsBankAccount = bankAccountRepository.existsByAccountNumber(accountNumber);

    return existsBankAccount.flatMap(resp -> {

      // Validate if Bank Account Exist
      if (resp) {
        return bankAccountMono.flatMap(bankAccount -> {

          // Validate if Bank Account belongs to Customer
          if (bankAccount.getNumIdentityDocCustomer().equals(numDocOwner)) {
            return Mono.just(true);
          } else {
            log.error("This bank Account does not belong to the customer");
            return Mono.error(new Exception("This bank Account does not belong to the customer"));
          }

        });
      } else {
        log.error("Bank Account not exists!!!");
        return Mono.error(new Exception("Bank Account not exist"));
      }

    });


  }

  @Override
  public Mono<BankAccount> updateBankAccountAmount(BankAccount bankAccount) {
    return bankAccountRepository.updateAmount(bankAccount);
  }

  @Override
  public Mono<BankAccount> findBankAccountByAccountNumber(String accountNumber) {
    return bankAccountRepository.findByAccountNumber(accountNumber);
  }

  @Override
  public Mono<CreditProduct> updateCreditProductDebtAmount(CreditProduct creditProduct) {

    Mono<Boolean> existsCreditProduct = creditProductRepository.existsByAccountNumber(creditProduct.getAccountNumber());

    return existsCreditProduct.flatMap(resp -> {

      if (resp) {
        return creditProductRepository.updateDebtAmount(creditProduct);
      } else {
        return Mono.error(new Exception("This Credit Product not exist"));
      }

    });

  }


  @Override
  public Mono<BankingMovement> deposit(BankingMovement bankingMovement) {

    Mono<Boolean> validateBankAccount = this.validateBankAccount(bankingMovement.getAccountNumber(),
            bankingMovement.getNumDocOwner());

    Mono<BankAccount> bankAccountMono = this.findBankAccountByAccountNumber(bankingMovement.getAccountNumber());


    return validateBankAccount.flatMap(resp -> {

      if (resp) {
        bankingMovement.setMovementDate(new Date());
        return bankAccountMono.flatMap(bankAccount -> {

          bankAccount.setAmountAvailable(bankAccount.getAmountAvailable() + bankingMovement.getAmount());
          return this.updateBankAccountAmount(bankAccount)
                  .then(movementRepository.save(bankingMovement));

        });
      } else {
        return Mono.error(new Exception("Error to validate bank Account"));
      }

    });
  }

  @Override
  public Mono<BankingMovement> withdraw(BankingMovement bankingMovement) {

    Mono<Boolean> validateBankAccount = this.validateBankAccount(bankingMovement.getAccountNumber(),
            bankingMovement.getNumDocOwner());

    Mono<BankAccount> bankAccountMono = this.findBankAccountByAccountNumber(bankingMovement.getAccountNumber());

    return validateBankAccount.flatMap(resp -> {

      if (resp) {
        bankingMovement.setMovementDate(new Date());
        return bankAccountMono.flatMap(bankAccount -> {

          bankAccount.setAmountAvailable(bankAccount.getAmountAvailable() - bankingMovement.getAmount());
          return this.updateBankAccountAmount(bankAccount)
                  .then(movementRepository.save(bankingMovement));

        });
      } else {
        return Mono.error(new Exception("Error to validate bank Account"));
      }

    });


  }

  @Override
  public Mono<BankingMovement> creditProductPayment(BankingMovement bankingMovement, String numAccountProductCredit) {

    Mono<Boolean> validateBankAccount = this.validateBankAccount(bankingMovement.getAccountNumber(),
            bankingMovement.getNumDocOwner());

    Mono<BankAccount> bankAccountMono = this.findBankAccountByAccountNumber(bankingMovement.getAccountNumber());

    Mono<CreditProduct> creditProductMono = creditProductRepository.findByAccountNumber(numAccountProductCredit);

    return validateBankAccount.flatMap(resp -> {

      if (resp) {
        bankingMovement.setMovementDate(new Date());
        return bankAccountMono.flatMap(bankAccount -> {

          bankAccount.setAmountAvailable(bankAccount.getAmountAvailable() - bankingMovement.getAmount());
          return this.updateBankAccountAmount(bankAccount)
                  .then(creditProductMono.flatMap(creditProduct -> {
                    creditProduct.setDebtAmount(creditProduct.getDebtAmount() - bankingMovement.getAmount());
                    return this.updateCreditProductDebtAmount(creditProduct);
                  }))
                  .then(movementRepository.save(bankingMovement));
        });
      } else {
        return Mono.error(new Exception("Error to validate bank Account"));
      }

    });
  }

  @Override
  public Mono<BankingMovement> findById(String id) {
    return movementRepository.findById(id);
  }

  @Override
  public Mono<BankingMovement> save(BankingMovement bankingMovement) {
    return movementRepository.save(bankingMovement);
  }

  @Override
  public Mono<BankingMovement> update(String id, BankingMovement bankingMovement) {
    return null;
  }

  @Override
  public Mono<BankingMovement> deleteById(String id) {
    return movementRepository.findById(id)
            .flatMap(bankingMovement -> movementRepository.delete(bankingMovement)
            .then(Mono.just(bankingMovement)));
  }


}
