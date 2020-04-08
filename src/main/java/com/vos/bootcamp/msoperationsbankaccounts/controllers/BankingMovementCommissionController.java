package com.vos.bootcamp.msoperationsbankaccounts.controllers;

import com.vos.bootcamp.msoperationsbankaccounts.models.BankingMovementCommission;
import com.vos.bootcamp.msoperationsbankaccounts.services.BankingMovementCommissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RestController
@RequestMapping("/api/bankingMovementsCommissions")
@Api(value = "Banking Movement Microservice")
public class BankingMovementCommissionController {

  private final BankingMovementCommissionService service;

  public BankingMovementCommissionController(BankingMovementCommissionService service) {
    this.service = service;
  }

  /* =========================================================================
  Function to obtain a BankingMovement Commissions by accountNumber and Dates
  ============================================================================= */
  @GetMapping("/report/dates/{date}/{date2}")
  @ApiOperation(value = "Get a Banking Movement Commissions By Dates", notes = "Get a banking Movement Commissions by Dates")
  public Flux<BankingMovementCommission> getBydDatesBankingMovementCommissions(
          @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
          @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date2) {
    return service.findByRangeCommissionDate(date, date2);
  }

  /* =========================================================================
  Function to obtain a BankingMovement Commissions by accountNumber and Dates
  ============================================================================= */
  @GetMapping("/report/{numberAccount}/dates/{date}/{date2}")
  @ApiOperation(value = "Get a Banking Movement Commissions", notes = "Get a banking Movement Commissions by Account Number")
  public Flux<BankingMovementCommission> getByAccountNumberAndDateBankingMovementCommissions(
          @PathVariable String numberAccount,
          @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
          @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date2) {
    return service.findByAccountNumberAndRangeCommissionDate(numberAccount, date, date2);
  }



}
