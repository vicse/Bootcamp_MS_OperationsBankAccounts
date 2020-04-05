package com.vos.bootcamp.msoperationsbankaccounts.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Lima")
  private Date commissionDate;


}
