package com.vos.bootcamp.msoperationsbankaccounts.services;

import com.vos.bootcamp.msoperationsbankaccounts.commons.Constant;
import com.vos.bootcamp.msoperationsbankaccounts.models.BankAccount;
import com.vos.bootcamp.msoperationsbankaccounts.models.BankingMovement;
import com.vos.bootcamp.msoperationsbankaccounts.models.BankingMovementCommission;
import com.vos.bootcamp.msoperationsbankaccounts.models.CreditProduct;
import com.vos.bootcamp.msoperationsbankaccounts.repositories.BankAccountRepository;
import com.vos.bootcamp.msoperationsbankaccounts.repositories.BankingMovementCommissionRepository;
import com.vos.bootcamp.msoperationsbankaccounts.repositories.BankingMovementRepository;
import com.vos.bootcamp.msoperationsbankaccounts.repositories.CreditProductRepository;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class BankingMovementServiceImpl implements BankingMovementService {

  private final BankingMovementRepository movementRepository;
  private final BankAccountRepository bankAccountRepository;
  private final CreditProductRepository creditProductRepository;
  private final BankingMovementCommissionRepository bankingMovementCommissionRepository;

  public BankingMovementServiceImpl(
          BankingMovementRepository movementRepository,
          BankAccountRepository bankAccountRepository,
          CreditProductRepository creditProductRepository,
          BankingMovementCommissionRepository bankingMovementCommissionRepository) {
    this.movementRepository = movementRepository;
    this.bankAccountRepository = bankAccountRepository;
    this.creditProductRepository = creditProductRepository;
    this.bankingMovementCommissionRepository = bankingMovementCommissionRepository;
  }

  @Override
  public Flux<BankingMovement> findAll() {
    return movementRepository.findAll();
  }

  @Override
  public Flux<BankingMovement> findByAccountNumberAndMovementDate(
          String accountNumber,Date date, Date date1) {
    return movementRepository
            .findByMovementDateIsBetweenAndAccountNumber(date, date1,accountNumber);
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

    Mono<Boolean> existsCreditProduct = creditProductRepository
            .existsByAccountNumber(creditProduct.getAccountNumber());

    return existsCreditProduct.flatMap(resp -> {

      if (resp) {
        return creditProductRepository.updateDebtAmount(creditProduct);
      } else {
        return Mono.error(new Exception("This Credit Product not exist"));
      }

    });

  }

  @Override
  public Mono<Boolean> chargeCommission(String accountNumber, Date date) {

    final BankingMovementCommission bankingMovementCommission = BankingMovementCommission
            .builder()
            .accountNumberOrigin(accountNumber)
            .commissionAmount(Constant.COMMISSION_AMOUNT)
            .commissionDate(new Date()).build();

    Mono<Number> numTransactionsInBankAccount = Mono.from(
            findByAccountNumberAndMovementDate(
                    accountNumber, date, Constant.addDayToADate(date, 1)
            )
            .count());

    return numTransactionsInBankAccount.flatMap(number -> {

      //If the number of transactions in the bank account is greater
      // than the maximum number of transactions per day
      if (number.intValue() > Constant.NUMBER_MAX_TRANSACTIONS) {
        return bankingMovementCommissionRepository.save(bankingMovementCommission)
                .then(Mono.just(true));
      } else {
        // Don't create a commission and Return false
        return Mono.just(false);
      }

    });

  }


  @Override
  public Mono<BankingMovement> deposit(BankingMovement bankingMovement) {

    Mono<Boolean> validateBankAccount = this.validateBankAccount(bankingMovement.getAccountNumber(),
            bankingMovement.getNumDocOwner());

    Mono<Boolean> chargeCommissionBool = this
            .chargeCommission(bankingMovement.getAccountNumber(), Constant.TODAY);

    Mono<BankAccount> bankAccount = this
            .findBankAccountByAccountNumber(bankingMovement.getAccountNumber());


    return validateBankAccount.flatMap(resp -> {

      if (resp) {

        bankingMovement.setMovementDate(new Date());
        return bankAccount.flatMap(bankAccountDB -> chargeCommissionBool.flatMap(commissionRes -> {

          if (commissionRes) {
            // Add amount Banking and subtract Commission Amount
            bankAccountDB.setAmountAvailable(
                    bankAccountDB.getAmountAvailable()
                    + bankingMovement.getAmount()
                    - Constant.COMMISSION_AMOUNT
            );

          } else {
            bankAccountDB.setAmountAvailable(
                    bankAccountDB.getAmountAvailable()
                    + bankingMovement.getAmount()
            );

          }

          return this.updateBankAccountAmount(bankAccountDB)
                  .then(movementRepository.save(bankingMovement));

        }));

      } else {
        return Mono.error(new Exception("Error to validate bank Account"));
      }

    });
  }

  @Override
  public Mono<BankingMovement> withdraw(BankingMovement bankingMovement) {

    Mono<Boolean> validateBankAccount = this.validateBankAccount(bankingMovement.getAccountNumber(),
            bankingMovement.getNumDocOwner());

    Mono<Boolean> chargeCommissionBool = this
            .chargeCommission(bankingMovement.getAccountNumber(), Constant.TODAY);

    Mono<BankAccount> bankAccount = this
            .findBankAccountByAccountNumber(bankingMovement.getAccountNumber());


    return validateBankAccount.flatMap(resp -> {

      if (resp) {
        bankingMovement.setMovementDate(new Date());
        return bankAccount.flatMap(bankAccountDB -> chargeCommissionBool.flatMap(commissionRes -> {

          if (commissionRes) {

            bankAccountDB.setAmountAvailable(
                    bankAccountDB.getAmountAvailable()
                    - bankingMovement.getAmount()
                    - Constant.COMMISSION_AMOUNT
            );

          } else {

            bankAccountDB.setAmountAvailable(
                    bankAccountDB.getAmountAvailable()
                    - bankingMovement.getAmount()
            );

          }

          return this.updateBankAccountAmount(bankAccountDB)
                  .then(movementRepository.save(bankingMovement));

        }));
      } else {
        return Mono.error(new Exception("Error to validate bank Account"));
      }

    });


  }

  @Override
  public Mono<BankingMovement> creditProductPayment(
          BankingMovement bankingMovement, String numAccountProductCredit) {

    Mono<Boolean> validateBankAccount = this
            .validateBankAccount(bankingMovement.getAccountNumber(),
            bankingMovement.getNumDocOwner());

    Mono<BankAccount> bankAccountMono = this
            .findBankAccountByAccountNumber(bankingMovement.getAccountNumber());

    Mono<CreditProduct> creditProductMono = creditProductRepository
            .findByAccountNumber(numAccountProductCredit);

    return validateBankAccount.flatMap(resp -> {

      if (resp) {
        bankingMovement.setMovementDate(new Date());
        return bankAccountMono.flatMap(bankAccount -> {

          bankAccount.setAmountAvailable(
                  bankAccount.getAmountAvailable()
                  - bankingMovement.getAmount()
          );

          return this.updateBankAccountAmount(bankAccount)
                  .then(creditProductMono.flatMap(creditProduct -> {

                    creditProduct.setDebtAmount(
                            creditProduct.getDebtAmount()
                            - bankingMovement.getAmount()
                    );

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
    return movementRepository.findById(id)
            .flatMap(bankingMovementDB -> {

              if (bankingMovement.getAmount() == null) {
                bankingMovementDB.setAmount(bankingMovementDB.getAmount());
              } else {
                bankingMovementDB.setAmount(bankingMovement.getAmount());
              }

              return movementRepository.save(bankingMovementDB);

            });

  }

  @Override
  public Mono<BankingMovement> deleteById(String id) {
    return movementRepository.findById(id)
            .flatMap(bankingMovement -> movementRepository.delete(bankingMovement)
            .then(Mono.just(bankingMovement)));
  }


}
