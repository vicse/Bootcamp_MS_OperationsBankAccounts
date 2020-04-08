package com.vos.bootcamp.msoperationsbankaccounts.services;

import com.vos.bootcamp.msoperationsbankaccounts.models.BankingMovementCommission;
import com.vos.bootcamp.msoperationsbankaccounts.utils.ICrud;
import java.util.Date;
import reactor.core.publisher.Flux;


public interface BankingMovementCommissionService extends ICrud<BankingMovementCommission> {

  public Flux<BankingMovementCommission> findByAccountNumberAndRangeCommissionDate(
          String accountNumber, Date date, Date date1);

  public Flux<BankingMovementCommission> findByRangeCommissionDate(
          Date date, Date date1);



}
