package com.vos.bootcamp.msoperationsbankaccounts.services;

import com.vos.bootcamp.msoperationsbankaccounts.models.*;
import com.vos.bootcamp.msoperationsbankaccounts.repositories.BankAccountRepository;
import com.vos.bootcamp.msoperationsbankaccounts.repositories.BankingMovementRepository;
import com.vos.bootcamp.msoperationsbankaccounts.repositories.CreditProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.reactivestreams.Publisher;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Date;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class BankingMovementServiceTest {

  private final BankAccountType bankAccountType1 = BankAccountType.builder().name("AHORROS").build();

  private final BankingMovementType bankingMovementType1 = BankingMovementType.builder().name("RETIRO").codeTypeMovement(100).build();
  private final BankingMovementType bankingMovementType2 = BankingMovementType.builder().name("DEPOSITO").codeTypeMovement(120).build();

  private final BankAccount bankAccount = BankAccount.builder().accountNumber("123-123-1223123")
          .numIdentityDocCustomer("75772936").amountAvailable(1000.0).bankAccountType(bankAccountType1).build();

  private final BankingMovement bankingMovement1 = BankingMovement.builder().accountNumber("123-123-1223123")
          .numDocOwner("75772936").amount(300.0).movementDate(new Date()).bankingMovementType(bankingMovementType1).build();
  private final BankingMovement bankingMovement2 = BankingMovement.builder().accountNumber("123-123-1223123")
          .numDocOwner("75772936").amount(100.0).movementDate(new Date()).bankingMovementType(bankingMovementType2).build();
  private final BankingMovement bankingMovement3 = BankingMovement.builder().accountNumber("123-123-1223123")
          .numDocOwner("75772936").amount(600.0).movementDate(new Date()).bankingMovementType(bankingMovementType1).build();

  private final CreditProductType creditProductType = CreditProductType.builder().name("CREDITO PERSONAL").build();

  private final CreditProduct creditProduct1 = CreditProduct.builder().numDocOwner("75772936")
          .accountNumber("1234-123123-123").creditAmount(1300.00).DebtAmount(1300.0).creditAmountAvailable(1300.0)
          .creditProductType(creditProductType).build();


  @Mock
  private BankingMovementRepository bankingMovementRepository;

  @Mock
  private BankAccountRepository bankAccountRepository;

  @Mock
  private CreditProductRepository creditProductRepository;

  private BankingMovementService bankingMovementService;

  @BeforeEach
  void SetUp(){
    bankingMovementService = new BankingMovementServiceImpl(bankingMovementRepository, bankAccountRepository, creditProductRepository) {
    };
  }

  @Test
  void getAll() {
    when(bankingMovementRepository.findAll()).thenReturn(Flux.just(bankingMovement1, bankingMovement2, bankingMovement3));

    Flux<BankingMovement> actual = bankingMovementService.findAll();

    assertResults(actual, bankingMovement1, bankingMovement2, bankingMovement3);
  }

  @Test
  void getByNumDocOwner() {
    when(bankingMovementRepository.findByNumDocOwner(bankingMovement1.getNumDocOwner()))
            .thenReturn(Flux.just(bankingMovement1, bankingMovement2, bankingMovement3));

    Flux<BankingMovement> actual = bankingMovementService.findByNumDocOwner(bankingMovement1.getNumDocOwner());

    assertResults(actual, bankingMovement1, bankingMovement2, bankingMovement3);
  }

  /*@Test
  void getByAccountNumberAndMovementDate() {
    when(bankingMovementRepository.findByMovementDateAndAccountNumber(new Date(), bankingMovement1.getAccountNumber()))
            .thenReturn(Flux.just(bankingMovement1, bankingMovement2, bankingMovement3));

    Flux<BankingMovement> actual = bankingMovementService
            .findByAccountNumberAndMovementDate(bankingMovement1.getAccountNumber(), new Date());

    assertResults(actual, bankingMovement1, bankingMovement2, bankingMovement3);
  }*/

  @Test
  void getById_whenIdExists_returnCorrectBankingMovement() {
    when(bankingMovementRepository.findById(bankingMovement1.getId())).thenReturn(Mono.just(bankingMovement1));

    Mono<BankingMovement> actual = bankingMovementService.findById(bankingMovement1.getId());

    assertResults(actual, bankingMovement1);
  }

  @Test
  void getById_whenIdNotExist_returnEmptyMono() {
    when(bankingMovementRepository.findById(bankingMovement1.getId())).thenReturn(Mono.empty());

    Mono<BankingMovement> actual = bankingMovementService.findById(bankingMovementType1.getId());

    assertResults(actual);
  }

  @Test
  void createBankingMovement() {
    when(bankingMovementRepository.save(bankingMovement1)).thenReturn(Mono.just(bankingMovement1));

    Mono<BankingMovement> actual = bankingMovementService.save(bankingMovement1);

    assertResults(actual, bankingMovement1);
  }

  @Test
  void depositAndCreateBankingMovement() {
    when(bankAccountRepository.existsByAccountNumber(bankAccount.getAccountNumber()))
            .thenReturn(Mono.just(true));
    when(bankAccountRepository.findByAccountNumber(bankAccount.getAccountNumber()))
            .thenReturn(Mono.just(bankAccount));
    when(bankAccountRepository.updateAmount(bankAccount)).thenReturn(Mono.just(bankAccount));
    when(bankingMovementRepository.save(bankingMovement1)).thenReturn(Mono.just(bankingMovement1));

    Mono<BankingMovement> actual = bankingMovementService.deposit(bankingMovement1);

    assertResults(actual, bankingMovement1);

  }

  @Test
  void depositAndCreateBankingMovement_WhenBankAccountNotExist() {
    when(bankAccountRepository.existsByAccountNumber(bankAccount.getAccountNumber()))
            .thenReturn(Mono.just(false));
    when(bankAccountRepository.findByAccountNumber(bankAccount.getAccountNumber()))
            .thenReturn(Mono.just(bankAccount));
    when(bankAccountRepository.updateAmount(bankAccount)).thenReturn(Mono.just(bankAccount));
    when(bankingMovementRepository.save(bankingMovement1)).thenReturn(Mono.just(bankingMovement1));

    Mono<BankingMovement> actual = bankingMovementService.deposit(bankingMovement1);

    assertResults(actual, new Exception("Bank Account not exist"));
  }

  @Test
  void withdrawAndCreateBankingMovement() {
    when(bankAccountRepository.existsByAccountNumber(bankAccount.getAccountNumber()))
            .thenReturn(Mono.just(true));
    when(bankAccountRepository.findByAccountNumber(bankAccount.getAccountNumber()))
            .thenReturn(Mono.just(bankAccount));
    when(bankAccountRepository.updateAmount(bankAccount)).thenReturn(Mono.just(bankAccount));
    when(bankingMovementRepository.save(bankingMovement1)).thenReturn(Mono.just(bankingMovement1));

    Mono<BankingMovement> actual = bankingMovementService.withdraw(bankingMovement1);

    assertResults(actual, bankingMovement1);
  }

  @Test
  void withdrawAndCreateBankingMovement_WhenBankAccountNotExist() {
    when(bankAccountRepository.existsByAccountNumber(bankAccount.getAccountNumber()))
            .thenReturn(Mono.just(false));
    when(bankAccountRepository.findByAccountNumber(bankAccount.getAccountNumber()))
            .thenReturn(Mono.just(bankAccount));
    when(bankAccountRepository.updateAmount(bankAccount)).thenReturn(Mono.just(bankAccount));
    when(bankingMovementRepository.save(bankingMovement1)).thenReturn(Mono.just(bankingMovement1));

    Mono<BankingMovement> actual = bankingMovementService.withdraw(bankingMovement1);

    assertResults(actual, new Exception("Bank Account not exist"));
  }

  @Test
  void withdrawAndCreateBankingMovement_WhenBankAccountNotExist_returnErrorValidate() {
    when(bankAccountRepository.existsByAccountNumber(bankAccount.getAccountNumber()))
            .thenReturn(Mono.just(false));
    when(bankAccountRepository.findByAccountNumber(bankAccount.getAccountNumber()))
            .thenReturn(Mono.empty());

    when(bankAccountRepository.updateAmount(bankAccount)).thenReturn(Mono.just(bankAccount));
    when(bankingMovementRepository.save(bankingMovement1)).thenReturn(Mono.just(bankingMovement1));

    Mono<BankingMovement> actual = bankingMovementService.withdraw(bankingMovement1);

    assertResults(actual, new Exception("Bank Account not exist"));
  }

  @Test
  void creditProductPaymentAndCreateBankingMovement() {
    when(bankAccountRepository.existsByAccountNumber(bankAccount.getAccountNumber()))
            .thenReturn(Mono.just(true));
    when(bankAccountRepository.findByAccountNumber(bankAccount.getAccountNumber()))
            .thenReturn(Mono.just(bankAccount));

    when(bankAccountRepository.updateAmount(bankAccount)).thenReturn(Mono.just(bankAccount));

    when(creditProductRepository.existsByAccountNumber(creditProduct1.getAccountNumber()))
            .thenReturn(Mono.just(true));

    when(creditProductRepository.findByAccountNumber(creditProduct1.getAccountNumber()))
            .thenReturn(Mono.just(creditProduct1));

    when(creditProductRepository.updateDebtAmount(creditProduct1)).thenReturn(Mono.just(creditProduct1));

    when(bankingMovementRepository.save(bankingMovement1)).thenReturn(Mono.just(bankingMovement1));

    Mono<BankingMovement> actual = bankingMovementService.creditProductPayment(bankingMovement1, creditProduct1.getAccountNumber());

    assertResults(actual, bankingMovement1);

  }

  @Test
  void update_whenIdExists_returnUpdatedBankingMovement() {
    when(bankingMovementRepository.findById(bankingMovement1.getId())).thenReturn(Mono.just(bankingMovement1));
    when(bankingMovementRepository.save(bankingMovement1)).thenReturn(Mono.just(bankingMovement1));

    Mono<BankingMovement> actual = bankingMovementService.update(bankingMovement1.getId(), bankingMovement1);

    assertResults(actual, bankingMovement1);
  }


  @Test
  void update_whenDataIsNull_returnUpdatedBankingMovement() {
    when(bankingMovementRepository.findById(bankingMovement1.getId())).thenReturn(Mono.just(bankingMovement1));
    when(bankingMovementRepository.save(bankingMovement1)).thenReturn(Mono.just(bankingMovement1));

    bankingMovement1.setAmount(null);

    Mono<BankingMovement> actual = bankingMovementService.update(bankingMovement1.getId(), bankingMovement1);

    assertResults(actual, bankingMovement1);
  }

  @Test
  void update_whenIdNotExist_returnEmptyMono() {
    when(bankingMovementRepository.findById(bankingMovement1.getId())).thenReturn(Mono.empty());

    Mono<BankingMovement> actual = bankingMovementService.update(bankingMovement1.getId(), bankingMovement1);

    assertResults(actual);
  }

  @Test
  void delete_whenBankingMovementExists_performDeletion() {
    when(bankingMovementRepository.findById(bankingMovement1.getId())).thenReturn(Mono.just(bankingMovement1));
    when(bankingMovementRepository.delete(bankingMovement1)).thenReturn(Mono.empty());

    Mono<BankingMovement> actual = bankingMovementService.deleteById(bankingMovement1.getId());

    assertResults(actual, bankingMovement1);
  }

  @Test
  void delete_whenIdNotExist_returnEmptyMono() {
    when(bankingMovementRepository.findById(bankingMovement1.getId())).thenReturn(Mono.empty());

    Mono<BankingMovement> actual = bankingMovementService.deleteById(bankingMovement1.getId());

    assertResults(actual);
  }

  @Test
  void updateCreditProductDebtAmount_WhenCreditProductExist() {
    when(creditProductRepository.existsByAccountNumber(creditProduct1.getAccountNumber()))
            .thenReturn(Mono.just(true));

    when(creditProductRepository.updateDebtAmount(creditProduct1))
            .thenReturn(Mono.just(creditProduct1));

    Mono<CreditProduct> creditProductMono = bankingMovementService.updateCreditProductDebtAmount(creditProduct1);

    assertResults(creditProductMono, creditProduct1);

  }

  @Test
  void updateCreditProductDebtAmount_WhenCreditProductNotExist() {
    when(creditProductRepository.existsByAccountNumber(creditProduct1.getAccountNumber()))
            .thenReturn(Mono.just(false));

    when(creditProductRepository.updateDebtAmount(creditProduct1))
            .thenReturn(Mono.just(creditProduct1));

    Mono<CreditProduct> creditProductMono = bankingMovementService.updateCreditProductDebtAmount(creditProduct1);

    assertResultsCreditProduct(creditProductMono, new Exception("This Credit Product not exist"));

  }


  /* ===============================================
           Validations of Bank Accounts
  ================================================== */
  @Test
  void validateBankAccount_WhenBankAccountBelongsToCustomer_returnTrue() {
    when(bankAccountRepository.existsByAccountNumber(bankAccount.getAccountNumber()))
            .thenReturn(Mono.just(true));

    when(bankAccountRepository.findByAccountNumber(bankAccount.getAccountNumber()))
            .thenReturn(Mono.just(bankAccount));

    Mono<Boolean> actual = bankingMovementService.validateBankAccount(bankAccount.getAccountNumber(), bankAccount.getNumIdentityDocCustomer());

    assertResults(actual, true);

  }

  @Test
  void validateBankAccount_WhenBankAccountNotExist_returnFalse() {
    when(bankAccountRepository.existsByAccountNumber(bankAccount.getAccountNumber()))
            .thenReturn(Mono.just(false));

    Mono<Boolean> actual = bankingMovementService.validateBankAccount(bankAccount.getAccountNumber(), bankAccount.getNumIdentityDocCustomer());

    assertResults(actual, new Exception("Bank Account not exist"));

  }

  @Test
  void validateBankAccount_WhenBankAccountNotBelongsToCustomer_returnFalse() {
    final String numDocCustomer = "NOT_EXIST";
    when(bankAccountRepository.existsByAccountNumber(bankAccount.getAccountNumber()))
            .thenReturn(Mono.just(true));

    when(bankAccountRepository.findByAccountNumber(bankAccount.getAccountNumber()))
            .thenReturn(Mono.just(bankAccount));

    Mono<Boolean> actual = bankingMovementService.validateBankAccount(bankAccount.getAccountNumber(), numDocCustomer);

    assertResults(actual, new Exception("This bank Account does not belong to the customer"));

  }

  @Test
  void getBankAccountByAccountNumber_whenAccountNumberExists_returnCorrectBankAccount() {
    when(bankAccountRepository.findByAccountNumber(bankAccount.getAccountNumber()))
            .thenReturn(Mono.just(bankAccount));

    Mono<BankAccount> actual = bankingMovementService.findBankAccountByAccountNumber(bankAccount.getAccountNumber());

    assertResults(actual, bankAccount);
  }

  @Test
  void getBankAccountByAccountNumber_whenIdNotExist_returnEmptyMono() {
    when(bankAccountRepository.findByAccountNumber(bankAccount.getAccountNumber()))
            .thenReturn(Mono.empty());

    Mono<BankAccount> actual = bankingMovementService.findBankAccountByAccountNumber(bankAccount.getAccountNumber());

    assertResults(actual);
  }

  @Test
  void updateAmountBankAccount_whenAccountNumberExists_returnUpdatedBankAccount() {
    when(bankAccountRepository.findByAccountNumber(bankAccount.getAccountNumber()))
            .thenReturn(Mono.just(bankAccount));
    when(bankAccountRepository.updateAmount(bankAccount)).thenReturn(Mono.just(bankAccount));

    Mono<BankAccount> actual = bankingMovementService.updateBankAccountAmount(bankAccount);

    assertResults(actual, bankAccount);
  }

  private void assertResults(Mono<Boolean> actual, Exception exception) {
    StepVerifier
            .create(actual)
            .expectErrorMessage(exception.getMessage())
            .verify();
  }


  private void assertResults(Publisher<BankingMovement> publisher, Exception exception) {
    StepVerifier
            .create(publisher)
            .expectErrorMessage(exception.getMessage())
            .verify();
  }

  private void assertResultsCreditProduct(Publisher<CreditProduct> publisher, Exception exception) {
    StepVerifier
            .create(publisher)
            .expectErrorMessage(exception.getMessage())
            .verify();
  }

  private void assertResults(Mono<Boolean> actual, boolean b) {
    StepVerifier
            .create(actual)
            .expectNext(b)
            .verifyComplete();
  }

  private void assertResults(Publisher<BankingMovement> publisher, BankingMovement... expectedBankingMovement) {
    StepVerifier
            .create(publisher)
            .expectNext(expectedBankingMovement)
            .verifyComplete();
  }

  private void assertResults(Publisher<CreditProduct> publisher, CreditProduct... expectedCreditProduct) {
    StepVerifier
            .create(publisher)
            .expectNext(expectedCreditProduct)
            .verifyComplete();
  }



  private void assertResults(Publisher<BankAccount> publisher, BankAccount... expectedBankAccount) {
    StepVerifier
            .create(publisher)
            .expectNext(expectedBankAccount)
            .verifyComplete();
  }




}
