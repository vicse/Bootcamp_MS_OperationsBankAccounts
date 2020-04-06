package com.vos.bootcamp.msoperationsbankaccounts.controllers;

import com.vos.bootcamp.msoperationsbankaccounts.models.BankingMovement;
import com.vos.bootcamp.msoperationsbankaccounts.models.BankingMovementType;
import com.vos.bootcamp.msoperationsbankaccounts.services.BankingMovementService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class BankingMovementControllerTest {

  @Mock
  private BankingMovementService bankingMovementService;
  private WebTestClient client;
  private List<BankingMovement> expectedBankingMovements;

  private final BankingMovementType bankingMovementType1 = BankingMovementType.builder().id("1").name("RETIRO").codeTypeMovement(100).build();
  private final BankingMovementType bankingMovementType2 = BankingMovementType.builder().id("2").name("DEPOSITO").codeTypeMovement(200).build();

  @BeforeEach
  void setUp() {
    client = WebTestClient
            .bindToController(new BankingMovementController(bankingMovementService))
            .configureClient()
            .baseUrl("/api/bankingMovements")
            .build();

    expectedBankingMovements = Arrays.asList(
            BankingMovement.builder().id("1").accountNumber("123-123-1223123")
                    .numDocOwner("75772936").amount(300.0).bankingMovementType(bankingMovementType1).build(),
            BankingMovement.builder().id("2").accountNumber("123-123-1223123")
                    .numDocOwner("75772936").amount(100.0).bankingMovementType(bankingMovementType2).build(),
            BankingMovement.builder().id("3").accountNumber("123-123-1223123")
                    .numDocOwner("75772936").amount(600.0).bankingMovementType(bankingMovementType1).build());

  }

  @Test
  void getAll() {
    when(bankingMovementService.findAll()).thenReturn(Flux.fromIterable(expectedBankingMovements));

    client.get()
            .uri("/")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBodyList(BankingMovement.class)
            .isEqualTo(expectedBankingMovements);
  }

  @Test
  void getBankingMovementById_whenBankingMovementExists_returnCorrectBankingMovement() {
    BankingMovement expectedBankingMovement = expectedBankingMovements.get(0);
    when(bankingMovementService.findById(expectedBankingMovement.getId()))
            .thenReturn(Mono.just(expectedBankingMovement));

    client.get()
            .uri("/{id}", expectedBankingMovement.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(BankingMovement.class)
            .isEqualTo(expectedBankingMovement);
  }

  @Test
  void getBankingMovementById_whenBankingMovementNotExist_returnNotFound() {
    String id = "NOT_EXIST_ID";
    when(bankingMovementService.findById(id)).thenReturn(Mono.empty());

    client.get()
            .uri("/{id}", id)
            .exchange()
            .expectStatus()
            .isNotFound();
  }

  @Test
  void addBankingMovementByDeposit() {
    BankingMovement expectedBankingMovement = expectedBankingMovements.get(0);
    when(bankingMovementService.deposit(expectedBankingMovement))
            .thenReturn(Mono.just(expectedBankingMovement));

    client.post()
            .uri("/deposit")
            .body(Mono.just(expectedBankingMovement), BankingMovement.class)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(BankingMovement.class)
            .isEqualTo(expectedBankingMovement);
  }

  @Test
  void addBankingMovementByWithdraw() {
    BankingMovement expectedBankingMovement = expectedBankingMovements.get(0);
    when(bankingMovementService.withdraw(expectedBankingMovement))
            .thenReturn(Mono.just(expectedBankingMovement));

    client.post()
            .uri("/withdraw")
            .body(Mono.just(expectedBankingMovement), BankingMovement.class)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(BankingMovement.class)
            .isEqualTo(expectedBankingMovement);
  }

  @Test
  void deleteBankingMovement_whenBankingMovementExists_performDeletion() {
    BankingMovement bankingMovementToDelete = expectedBankingMovements.get(0);
    when(bankingMovementService.deleteById(bankingMovementToDelete.getId()))
            .thenReturn(Mono.just(bankingMovementToDelete));

    client.delete()
            .uri("/{id}", bankingMovementToDelete.getId())
            .exchange()
            .expectStatus()
            .isOk();
  }

  @Test
  void deleteBankingMovement_whenIdNotExist_returnNotFound() {
    BankingMovement bankingMovementToDelete = expectedBankingMovements.get(0);
    when(bankingMovementService.deleteById(bankingMovementToDelete.getId()))
            .thenReturn(Mono.empty());

    client.delete()
            .uri("/{id}", bankingMovementToDelete.getId())
            .exchange()
            .expectStatus()
            .isNotFound();
  }


}
