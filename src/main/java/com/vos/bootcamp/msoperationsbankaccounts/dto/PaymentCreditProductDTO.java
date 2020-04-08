package com.vos.bootcamp.msoperationsbankaccounts.dto;

import com.vos.bootcamp.msoperationsbankaccounts.models.BankingMovement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PaymentCreditProductDTO {

  private BankingMovement bankingMovement;

  private String numberAccountCreditProduct;

}
