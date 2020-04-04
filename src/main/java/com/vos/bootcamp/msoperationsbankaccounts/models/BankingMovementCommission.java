package com.vos.bootcamp.msoperationsbankaccounts.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Document(collection = "ms_bankingMovements_commissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BankingMovementCommission {

  @Id
  private String id;

  @NotBlank(message = "'accountNumberOrigin' is required")
  private String accountNumberOrigin;

  private double commissionAmount;

  private Date commissionDate;


}
