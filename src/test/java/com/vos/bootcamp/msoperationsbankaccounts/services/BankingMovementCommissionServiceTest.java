package com.vos.bootcamp.msoperationsbankaccounts.services;

import com.vos.bootcamp.msoperationsbankaccounts.models.BankingMovementCommission;
import com.vos.bootcamp.msoperationsbankaccounts.repositories.BankingMovementCommissionRepository;;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.reactivestreams.Publisher;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class BankingMovementCommissionServiceTest {

  private final BankingMovementCommission bankingMovementCommission1 = BankingMovementCommission.builder()
          .accountNumberOrigin("1231-1311-12222").commissionAmount(20.0).build();
  private final BankingMovementCommission bankingMovementCommission2 = BankingMovementCommission.builder()
          .accountNumberOrigin("1231-1311-12222").commissionAmount(20.0).build();
  private final BankingMovementCommission bankingMovementCommission3 = BankingMovementCommission.builder()
          .accountNumberOrigin("1231-1311-12222").commissionAmount(20.0).build();

  @Mock
  private BankingMovementCommissionRepository bankingMovementCommissionRepository;

  private BankingMovementCommissionService bankingMovementCommissionService;

  @BeforeEach
  void SetUp(){
    bankingMovementCommissionService = new BankingMovementCommissionsServiceImpl(bankingMovementCommissionRepository) {
    };
  }

  @Test
  void getAll() {
    when(bankingMovementCommissionRepository.findAll()).thenReturn(Flux.just(bankingMovementCommission1, bankingMovementCommission2, bankingMovementCommission3));

    Flux<BankingMovementCommission> actual = bankingMovementCommissionService.findAll();

    assertResults(actual, bankingMovementCommission1, bankingMovementCommission2, bankingMovementCommission3);
  }

  @Test
  void getById_whenIdExists_returnCorrectBankingMovementCommission() {
    when(bankingMovementCommissionRepository.findById(bankingMovementCommission1.getId())).thenReturn(Mono.just(bankingMovementCommission1));

    Mono<BankingMovementCommission> actual = bankingMovementCommissionService.findById(bankingMovementCommission1.getId());

    assertResults(actual, bankingMovementCommission1);
  }

  @Test
  void getById_whenIdNotExist_returnEmptyMono() {
    when(bankingMovementCommissionRepository.findById(bankingMovementCommission1.getId())).thenReturn(Mono.empty());

    Mono<BankingMovementCommission> actual = bankingMovementCommissionService.findById(bankingMovementCommission1.getId());

    assertResults(actual);
  }

  @Test
  void create() {
    when(bankingMovementCommissionRepository.save(bankingMovementCommission1)).thenReturn(Mono.just(bankingMovementCommission1));

    Mono<BankingMovementCommission> actual = bankingMovementCommissionService.save(bankingMovementCommission1);

    assertResults(actual, bankingMovementCommission1);
  }

  @Test
  void update_whenIdExists_returnUpdatedBankingMovementCommission() {
    when(bankingMovementCommissionRepository.findById(bankingMovementCommission1.getId())).thenReturn(Mono.just(bankingMovementCommission1));
    when(bankingMovementCommissionRepository.save(bankingMovementCommission1)).thenReturn(Mono.just(bankingMovementCommission1));

    Mono<BankingMovementCommission> actual = bankingMovementCommissionService.update(bankingMovementCommission1.getId(), bankingMovementCommission1);

    assertResults(actual, bankingMovementCommission1);
  }

  @Test
  void update_whenIdNotExist_returnEmptyMono() {
    when(bankingMovementCommissionRepository.findById(bankingMovementCommission1.getId())).thenReturn(Mono.empty());

    Mono<BankingMovementCommission> actual = bankingMovementCommissionService.update(bankingMovementCommission1.getId(), bankingMovementCommission1);

    assertResults(actual);
  }

  @Test
  void delete_whenBankingMovementCommissionExists_performDeletion() {
    when(bankingMovementCommissionRepository.findById(bankingMovementCommission1.getId())).thenReturn(Mono.just(bankingMovementCommission1));
    when(bankingMovementCommissionRepository.delete(bankingMovementCommission1)).thenReturn(Mono.empty());

    Mono<BankingMovementCommission> actual = bankingMovementCommissionService.deleteById(bankingMovementCommission1.getId());

    assertResults(actual, bankingMovementCommission1);
  }

  @Test
  void delete_whenIdNotExist_returnEmptyMono() {
    when(bankingMovementCommissionRepository.findById(bankingMovementCommission1.getId())).thenReturn(Mono.empty());

    Mono<BankingMovementCommission> actual = bankingMovementCommissionService.deleteById(bankingMovementCommission1.getId());

    assertResults(actual);
  }

  private void assertResults(Publisher<BankingMovementCommission> publisher, BankingMovementCommission... expectedBankingMovementCommission) {
    StepVerifier
            .create(publisher)
            .expectNext(expectedBankingMovementCommission)
            .verifyComplete();
  }


}
