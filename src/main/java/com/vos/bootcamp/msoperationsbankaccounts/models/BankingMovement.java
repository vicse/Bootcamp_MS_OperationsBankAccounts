package com.vos.bootcamp.msoperationsbankaccounts.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Document(collection = "ms_bankingMovements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BankingMovement {

  @Id
  private String id;

  @NotBlank(message = "'accountNumberOrigin' is required")
  private String accountNumberOrigin;

  @NotBlank(message = "'accountNumberDestination is required")
  private String accountNumberDestination;

  @NotBlank(message = "'numDocOwner' is required")
  private String numDocOwner;

  private Double amount;


  @Valid
  @DBRef
  private BankingMovementType bankingMovementType;

  private Date movementDate;


}
