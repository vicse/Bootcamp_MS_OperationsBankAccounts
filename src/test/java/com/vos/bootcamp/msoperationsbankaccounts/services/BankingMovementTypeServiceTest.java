package com.vos.bootcamp.msoperationsbankaccounts.services;

import com.vos.bootcamp.msoperationsbankaccounts.models.BankingMovementType;
import com.vos.bootcamp.msoperationsbankaccounts.repositories.BankingMovementTypeRepository;
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
public class BankingMovementTypeServiceTest {

  private final BankingMovementType bankingMovementType1 = BankingMovementType.builder().name("RETIRO").build();
  private final BankingMovementType bankingMovementType2 = BankingMovementType.builder().name("DEPOSITO").build();
  private final BankingMovementType bankingMovementType3 = BankingMovementType.builder().name("PAGO TARJETA DE CREDITO").build();

  @Mock
  private BankingMovementTypeRepository bankingMovementTypeRepository;

  private BankingMovementTypeService bankingMovementTypeService;


  @BeforeEach
  void SetUp(){
    bankingMovementTypeService = new BankingMovementTypeServiceImpl(bankingMovementTypeRepository) {
    };
  }

  @Test
  void getAll() {
    when(bankingMovementTypeRepository.findAll()).thenReturn(Flux.just(bankingMovementType1, bankingMovementType2, bankingMovementType3));

    Flux<BankingMovementType> actual = bankingMovementTypeService.findAll();

    assertResults(actual, bankingMovementType1, bankingMovementType2, bankingMovementType3);
  }

  @Test
  void getById_whenIdExists_returnCorrectBankingMovementType() {
    when(bankingMovementTypeRepository.findById(bankingMovementType1.getId())).thenReturn(Mono.just(bankingMovementType1));

    Mono<BankingMovementType> actual = bankingMovementTypeService.findById(bankingMovementType1.getId());

    assertResults(actual, bankingMovementType1);
  }

  @Test
  void getById_whenIdNotExist_returnEmptyMono() {
    when(bankingMovementTypeRepository.findById(bankingMovementType1.getId())).thenReturn(Mono.empty());

    Mono<BankingMovementType> actual = bankingMovementTypeService.findById(bankingMovementType1.getId());

    assertResults(actual);
  }

  @Test
  void create() {
    when(bankingMovementTypeRepository.save(bankingMovementType1)).thenReturn(Mono.just(bankingMovementType1));

    Mono<BankingMovementType> actual = bankingMovementTypeService.save(bankingMovementType1);

    assertResults(actual, bankingMovementType1);
  }

  @Test
  void update_whenIdExists_returnUpdatedBankingMovementType() {
    when(bankingMovementTypeRepository.findById(bankingMovementType1.getId())).thenReturn(Mono.just(bankingMovementType1));
    when(bankingMovementTypeRepository.save(bankingMovementType1)).thenReturn(Mono.just(bankingMovementType1));

    Mono<BankingMovementType> actual = bankingMovementTypeService.update(bankingMovementType1.getId(), bankingMovementType1);

    assertResults(actual, bankingMovementType1);
  }

  @Test
  void update_whenIdNotExist_returnEmptyMono() {
    when(bankingMovementTypeRepository.findById(bankingMovementType1.getId())).thenReturn(Mono.empty());

    Mono<BankingMovementType> actual = bankingMovementTypeService.update(bankingMovementType1.getId(), bankingMovementType1);

    assertResults(actual);
  }

  @Test
  void delete_whenBankingMovementTypeExists_performDeletion() {
    when(bankingMovementTypeRepository.findById(bankingMovementType1.getId())).thenReturn(Mono.just(bankingMovementType1));
    when(bankingMovementTypeRepository.delete(bankingMovementType1)).thenReturn(Mono.empty());

    Mono<BankingMovementType> actual = bankingMovementTypeService.deleteById(bankingMovementType1.getId());

    assertResults(actual, bankingMovementType1);
  }

  @Test
  void delete_whenIdNotExist_returnEmptyMono() {
    when(bankingMovementTypeRepository.findById(bankingMovementType1.getId())).thenReturn(Mono.empty());

    Mono<BankingMovementType> actual = bankingMovementTypeService.deleteById(bankingMovementType1.getId());

    assertResults(actual);
  }

  private void assertResults(Publisher<BankingMovementType> publisher, BankingMovementType... bankingMovementCommission1) {
    StepVerifier
            .create(publisher)
            .expectNext(bankingMovementCommission1)
            .verifyComplete();
  }




}
