package com.vos.bootcamp.msoperationsbankaccounts.controllers;

import com.vos.bootcamp.msoperationsbankaccounts.models.BankingMovementType;
import com.vos.bootcamp.msoperationsbankaccounts.services.BankingMovementTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class BankingMovementTypeControllerTest {

  @Mock
  private BankingMovementTypeService bankingMovementTypeService;
  private WebTestClient client;
  private List<BankingMovementType> expectedBankingMovementTypes;

  @BeforeEach
  void setUp() {
    client = WebTestClient
            .bindToController(new BankingMovementTypeController(bankingMovementTypeService))
            .configureClient()
            .baseUrl("/api/bankingMovementTypes")
            .build();

    expectedBankingMovementTypes = Arrays.asList(
            BankingMovementType.builder().id("1").name("DEPOSITO").codeTypeMovement(100).build(),
            BankingMovementType.builder().id("2").name("RETIRO").codeTypeMovement(200).build());

  }

  @Test
  void getAll() {
    when(bankingMovementTypeService.findAll()).thenReturn(Flux.fromIterable(expectedBankingMovementTypes));

    client.get()
            .uri("/")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBodyList(BankingMovementType.class)
            .isEqualTo(expectedBankingMovementTypes);
  }

  @Test
  void getBankingMovementTypeById_whenBankingMovementTypeExists_returnCorrectBankingMovementType() {
    BankingMovementType expectedBankingMovementType = expectedBankingMovementTypes.get(0);
    when(bankingMovementTypeService.findById(expectedBankingMovementType.getId()))
            .thenReturn(Mono.just(expectedBankingMovementType));

    client.get()
            .uri("/{id}", expectedBankingMovementType.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(BankingMovementType.class)
            .isEqualTo(expectedBankingMovementType);
  }

  @Test
  void getBankingMovementTypeById_whenBankingMovementTypeNotExist_returnNotFound() {
    String id = "NOT_EXIST_ID";
    when(bankingMovementTypeService.findById(id)).thenReturn(Mono.empty());

    client.get()
            .uri("/{id}", id)
            .exchange()
            .expectStatus()
            .isNotFound();
  }

  @Test
  void addBankingMovementType() {
    BankingMovementType expectedBankingMovementType = expectedBankingMovementTypes.get(0);
    when(bankingMovementTypeService.save(expectedBankingMovementType))
            .thenReturn(Mono.just(expectedBankingMovementType));

    client.post()
            .uri("/")
            .body(Mono.just(expectedBankingMovementType), BankingMovementType.class)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(BankingMovementType.class)
            .isEqualTo(expectedBankingMovementType);
  }

  @Test
  void updateBankingMovementType_whenBankingMovementTypeExists_performUpdate() {
    BankingMovementType expectedBankingMovementType = expectedBankingMovementTypes.get(0);
    when(bankingMovementTypeService.update(expectedBankingMovementType.getId(), expectedBankingMovementType))
            .thenReturn(Mono.just(expectedBankingMovementType));

    client.put()
            .uri("/{id}", expectedBankingMovementType.getId())
            .body(Mono.just(expectedBankingMovementType), BankingMovementType.class)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(BankingMovementType.class)
            .isEqualTo(expectedBankingMovementType);
  }

  @Test
  void updateBankingMovementType_whenBankingMovementTypeNotExist_returnNotFound() {
    String id = "NOT_EXIST_ID";
    BankingMovementType expectedBankingMovementType = expectedBankingMovementTypes.get(0);
    when(bankingMovementTypeService.update(id, expectedBankingMovementType)).thenReturn(Mono.empty());

    client.put()
            .uri("/{id}", id)
            .body(Mono.just(expectedBankingMovementType), BankingMovementType.class)
            .exchange()
            .expectStatus()
            .isNotFound();
  }

  @Test
  void deleteBankingMovementType_whenBankingMovementTypeExists_performDeletion() {
    BankingMovementType BankingMovementTypeToDelete = expectedBankingMovementTypes.get(0);
    when(bankingMovementTypeService.deleteById(BankingMovementTypeToDelete.getId()))
            .thenReturn(Mono.just(BankingMovementTypeToDelete));

    client.delete()
            .uri("/{id}", BankingMovementTypeToDelete.getId())
            .exchange()
            .expectStatus()
            .isOk();
  }

  @Test
  void deleteBankingMovementType_whenIdNotExist_returnNotFound() {
    BankingMovementType BankingMovementTypeToDelete = expectedBankingMovementTypes.get(0);
    when(bankingMovementTypeService.deleteById(BankingMovementTypeToDelete.getId()))
            .thenReturn(Mono.empty());

    client.delete()
            .uri("/{id}", BankingMovementTypeToDelete.getId())
            .exchange()
            .expectStatus()
            .isNotFound();
  }



}
