package com.zerobase.fintech.transaction.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchForm {

  private String accountNumber;
  private int deposit;
  private int withdraw;
  private String transactionName;
  private LocalDateTime createAt;

  public static SearchForm fromDto(TransactionEntity transactionEntity) {
    return SearchForm.builder()
        .accountNumber(transactionEntity.getAccountNumber().getAccountNumber())
        .deposit(transactionEntity.getDeposit())
        .withdraw(transactionEntity.getWithdraw())
        .transactionName(transactionEntity.getTransactionName())
        .createAt(transactionEntity.getCreateAt())
        .build();
  }
}