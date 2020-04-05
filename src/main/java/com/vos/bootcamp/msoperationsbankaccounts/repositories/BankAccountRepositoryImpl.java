package com.vos.bootcamp.msoperationsbankaccounts.repositories;

import com.vos.bootcamp.msoperationsbankaccounts.models.BankAccount;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class BankAccountRepositoryImpl implements BankAccountRepository {

  private WebClient webClient = WebClient.create("http://localhost:8002/api/bankAccounts");

  @Override
  public Mono<BankAccount> findByAccountNumber(String accountNumber) {
    return webClient
            .get()
            .uri("/accountNumber/" + accountNumber)
            .retrieve()
            .bodyToMono(BankAccount.class)
            ;
  }

  @Override
  public Mono<Boolean> existsByAccountNumber(String accountNumber) {
    return webClient
            .get()
            .uri("/" + accountNumber + "/exist")
            .retrieve()
            .bodyToMono(Boolean.class)
            ;
  }

  @Override
  public Mono<BankAccount> updateAmount(BankAccount bankAccount) {

      return webClient
              .put()
              .uri("/" + bankAccount.getId())
              .body(Mono.just(bankAccount), BankAccount.class)
              .retrieve()
              .bodyToMono(BankAccount.class);


  }

}
