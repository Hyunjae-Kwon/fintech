package com.zerobase.fintech.transaction.entity;

import com.zerobase.fintech.account.entity.AccountEntity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TransactionForm {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Request {

    private AccountEntity accountNumber;
    private int amount;
    private String userId;
    private String password;
    private AccountEntity toAccountNumber;
    private String transactionName;
    private boolean verify;
    private LocalDateTime createAt;

    public static TransactionEntity toDepositEntity(Request request) {
      return TransactionEntity.builder()
          .accountNumber(request.getToAccountNumber())
          .deposit(request.getAmount())
          .transactionName(request.getTransactionName())
          .verify(true)
          .createAt(LocalDateTime.now())
          .build();
    }

    public static TransactionEntity toWithdrawEntity(Request request) {
      return TransactionEntity.builder()
          .accountNumber(request.getAccountNumber())
          .withdraw(request.getAmount())
          .transactionName(request.getTransactionName())
          .verify(true)
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
    private int amount;
    private String transactionName;
    private LocalDateTime createAt;

    public static Response fromDepositDto(TransactionDto transactionDto) {
      return Response.builder()
          .accountNumber(transactionDto.getAccountNumber())
          .amount(transactionDto.getDeposit())
          .transactionName(transactionDto.getTransactionName())
          .createAt(transactionDto.getCreateAt())
          .build();
    }

    public static Response fromWithdrawDto(TransactionDto transactionDto) {
      return Response.builder()
          .accountNumber(transactionDto.getAccountNumber())
          .amount(transactionDto.getDeposit())
          .transactionName(transactionDto.getTransactionName())
          .createAt(transactionDto.getCreateAt())
          .build();
    }
  }
}
