package com.zerobase.fintech.transaction.entity;

import com.zerobase.fintech.account.entity.AccountEntity;
import jakarta.validation.constraints.Positive;
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

    @Positive(message = "1원 이상의 금액을 입력하세요.")
    private int amount;

    private String transactionName;
    private boolean verify;
    private LocalDateTime createAt;

    public static TransactionEntity toEntity(Request request) {
      return TransactionEntity.builder()
          .accountNumber(request.getAccountNumber())
          .deposit(request.getAmount())
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

    public static Response fromDto(TransactionDto transactionDto) {
      return Response.builder()
          .accountNumber(transactionDto.getAccountNumber())
          .amount(transactionDto.getDeposit())
          .transactionName(transactionDto.getTransactionName())
          .createAt(transactionDto.getCreateAt())
          .build();
    }
  }
}
