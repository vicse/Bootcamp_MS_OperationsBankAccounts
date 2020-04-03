package com.vos.bootcamp.msoperationsbankaccounts.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ms_bankingMovement_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BankingMovementType {

  @Id
  private String id;

  @NotBlank(message = "'Names' are required")
  private String name;

  @NotNull(message = "'CodeTypeMovement' cannot be null")
  private Integer codeTypeMovement;


}
