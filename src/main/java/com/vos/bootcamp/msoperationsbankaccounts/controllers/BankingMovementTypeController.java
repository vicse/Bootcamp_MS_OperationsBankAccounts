package com.vos.bootcamp.msoperationsbankaccounts.controllers;

import com.vos.bootcamp.msoperationsbankaccounts.models.BankingMovementType;
import com.vos.bootcamp.msoperationsbankaccounts.services.BankingMovementTypeService;
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
@RequestMapping("/api/bankingMovementTypes")
@Api(value = "Banking Movement Microservice")
public class BankingMovementTypeController {

  private final BankingMovementTypeService service;

  public BankingMovementTypeController(BankingMovementTypeService service) {
    this.service = service;
  }

  /* =========================================
      Function to List all Banking Movement Types
    =========================================== */
  @GetMapping
  @ApiOperation(value = "List all BankingMovementTypes", notes = "List all BankingMovementTypes of Collections")
  public Flux<BankingMovementType> getBankingMovementTypes() {
    return service.findAll();
  }

  /* ===============================================
       Function to obtain a BankingMovementType by id
  ================================================== */
  @GetMapping("/{id}")
  @ApiOperation(value = "Get a Credit Movement Type", notes = "Get a banking Movement Type by id")
  public Mono<ResponseEntity<BankingMovementType>> getByIdBankingMovementType(@PathVariable String id) {
    return service.findById(id)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity
                    .notFound()
                    .build()
            );
  }

  /* ===============================================
       Function to create a BankingMovementType
  =============================================== */
  @PostMapping
  @ApiOperation(value = "Create Credit Movement Type", notes = "Create BankingMovementType, check the model please")
  public Mono<ResponseEntity<BankingMovementType>> createBankingMovementType(
          @Valid @RequestBody BankingMovementType bankingMovementType) {
    return service.save(bankingMovementType)
            .map(bankingMovementTypeDB -> ResponseEntity
                    .created(URI.create("/api/bankingMovementTypes/".concat(bankingMovementTypeDB.getId())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(bankingMovementTypeDB)
            );
  }

  /* ====================================================
            Function to update a BankingMovementType by id
    ===================================================== */
  @PutMapping("/{id}")
  @ApiOperation(value = "Update a BankingMovementType", notes = "Update a banking movement Type by ID")
  public Mono<ResponseEntity<BankingMovementType>> updateBankingMovementType(
          @PathVariable String id,
          @RequestBody BankingMovementType bankingMovementType) {

    return service.update(id, bankingMovementType)
            .map(bankingMovementTypeDB -> ResponseEntity
                    .created(URI.create("/api/bankingMovementTypes/".concat(bankingMovementTypeDB.getId())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(bankingMovementTypeDB))
            .defaultIfEmpty(ResponseEntity
                    .notFound()
                    .build()
            );
  }

  /* ====================================================
            Function to delete a BankingMovementType by id
    ===================================================== */

  @DeleteMapping("/{id}")
  @ApiOperation(value = "Delete a BankingMovementType", notes = "Delete a banking Movement Type by ID")
  public Mono<ResponseEntity<Void>> deleteByIdBankingMovementType(@PathVariable String id) {
    return service.deleteById(id)
            .map(res -> ResponseEntity.ok().<Void>build())
            .defaultIfEmpty(ResponseEntity
                    .notFound()
                    .build()
            );

  }

}
