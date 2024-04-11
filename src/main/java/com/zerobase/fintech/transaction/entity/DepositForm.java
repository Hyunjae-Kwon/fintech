package com.zerobase.fintech.transaction.entity;

import com.zerobase.fintech.account.entity.AccountEntity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class DepositForm {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Request {

    private AccountEntity accountNumber;
    private int deposit;
    private String transactionName;
    private LocalDateTime createAt;

    public static TransactionEntity toEntity(Request request) {
      return TransactionEntity.builder()
          .accountNumber(request.getAccountNumber())
          .deposit(request.getDeposit())
          .transactionName(request.getTransactionName())
          .createAt(LocalDateTime.now())
          .build();
    }
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Response {

    private AccountEntity accountNumber;
    private int deposit;
    private String transactionName;
    private LocalDateTime createAt;

    public static Response fromDto(TransactionDto transactionDto) {
      return Response.builder()
          .accountNumber(transactionDto.getAccountNumber())
          .deposit(transactionDto.getDeposit())
          .transactionName(transactionDto.getTransactionName())
          .createAt(transactionDto.getCreateAt())
          .build();
    }
  }
}
