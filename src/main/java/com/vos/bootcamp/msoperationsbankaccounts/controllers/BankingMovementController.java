package com.vos.bootcamp.msoperationsbankaccounts.controllers;


import com.vos.bootcamp.msoperationsbankaccounts.dto.PaymentCreditProductDTO;
import com.vos.bootcamp.msoperationsbankaccounts.models.BankingMovement;
import com.vos.bootcamp.msoperationsbankaccounts.services.BankingMovementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/bankingMovements")
@Api(value = "Banking Movement Microservice")
public class BankingMovementController {

  private final BankingMovementService service;

  public BankingMovementController(BankingMovementService service) {
    this.service = service;
  }

  /* =========================================
    Function to List all Banking Movements
  =========================================== */
  @GetMapping
  @ApiOperation(value = "List all BankingMovements", notes = "List all BankingMovements of Collections")
  public Flux<BankingMovement> getBankingMovements() {
    return service.findAll();
  }

  /* =======================================================
   Function to List all Banking Movements By NumDocCustomer
  ========================================================== */
  @GetMapping("/numDocCustomer/{numDoc}")
  @ApiOperation(value = "List all BankingMovements by NumDocCustomer", notes = "List all BankingMovements ny NumDocCustomer of Collections")
  public Flux<BankingMovement> getBankingMovementsByNumDocCustomer(@PathVariable String numDoc) {
    return service.findByNumDocOwner(numDoc)
            .switchIfEmpty(Mono.error(new Exception("This customer not have banking movements")));
  }

  /* ===============================================
      Function to obtain a BankingMovement by id
  ================================================== */
  @GetMapping("/{id}")
  @ApiOperation(value = "Get a Banking Movement", notes = "Get a banking Movement by id")
  public Mono<ResponseEntity<BankingMovement>> getByIdBankingMovement(@PathVariable String id) {
    return service.findById(id)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity
                    .notFound()
                    .build()
            );
  }

  /* ===============================================
     Function to create banking Movement by deposit
  ================================================== */
  @PostMapping("/deposit")
  @ApiOperation(value = "Create Banking Movement by Deposit", notes = "Create BankingMovement by Deposit, check the model please")
  public Mono<ResponseEntity<BankingMovement>> createBankingMovementByDeposit(
          @Valid @RequestBody BankingMovement bankingMovement) {
    return service.deposit(bankingMovement)
            .map(bankingMovementDB -> ResponseEntity
                    .created(URI.create("/api/bankingMovements/".concat(bankingMovementDB.getId())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(bankingMovementDB)
            );
  }

  /* ===============================================
    Function to create banking Movement by Withdraw
 ================================================== */
  @PostMapping("/withdraw")
  @ApiOperation(value = "Create Banking Movement by Withdraw", notes = "Create BankingMovement by Withdraw, check the model please")
  public Mono<ResponseEntity<BankingMovement>> createBankingMovementByWithdraw(
          @Valid @RequestBody BankingMovement bankingMovement) {
    return service.withdraw(bankingMovement)
            .map(bankingMovementDB -> ResponseEntity
                    .created(URI.create("/api/bankingMovements/".concat(bankingMovementDB.getId())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(bankingMovementDB)
            );
  }

  /* =============================================================
    Function to create banking Movement by Payment Credit Product
  ================================================================ */
  @PostMapping("/paymentCreditProduct/numberAccount/{numAccount}")
  @ApiOperation(value = "Create Banking Movement by payment credit Product", notes =
          "Create BankingMovement by payment credit Product, check the model please")
  public Mono<ResponseEntity<BankingMovement>> createBankingMovementByPaymentCreditProduct(
          @PathVariable String numAccount,
          @Valid @RequestBody BankingMovement bankingMovement) {
    return service.creditProductPayment(bankingMovement, numAccount)
            .map(bankingMovementDB -> ResponseEntity
                    .created(URI.create("/api/bankingMovements/".concat(bankingMovementDB.getId())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(bankingMovementDB)
            );
  }

  /* ====================================================
          Function to delete a BankingMovement by id
    ===================================================== */

  @DeleteMapping("/{id}")
  @ApiOperation(value = "Delete a BankingMovement", notes = "Delete a banking Movement by ID")
  public Mono<ResponseEntity<Void>> deleteByIdBankingMovement(@PathVariable String id) {
    return service.deleteById(id)
            .map(res -> ResponseEntity.ok().<Void>build())
            .defaultIfEmpty(ResponseEntity
                    .notFound()
                    .build()
            );

  }




}
