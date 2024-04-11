package com.zerobase.fintech.transaction.entity;

import com.zerobase.fintech.account.entity.AccountEntity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto {

  private int transactionId;
  private AccountEntity accountNumber;
  private int deposit;
  private int withdraw;
  private String transactionName;
  private boolean verify;
  private LocalDateTime createAt;

  public static TransactionDto from(TransactionEntity transactionEntity) {
    return TransactionDto.builder()
        .accountNumber(transactionEntity.getAccountNumber())
        .deposit(transactionEntity.getDeposit())
        .withdraw(transactionEntity.getWithdraw())
        .transactionName(transactionEntity.getTransactionName())
        .verify(transactionEntity.isVerify())
        .createAt(transactionEntity.getCreateAt())
        .build();
  }
}
