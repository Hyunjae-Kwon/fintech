package com.zerobase.fintech.transaction.entity;

import com.zerobase.fintech.account.entity.AccountEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class WithdrawForm {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Request {

    private AccountEntity accountNumber;

    @Positive(message = "0원 이상의 금액을 입력하세요.")
    private int amount;

    @NotBlank(message = "아이디를 입력하세요.")
    private String userId;

    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;

    private String transactionName;
    private boolean verify;
    private LocalDateTime createAt;

    public static TransactionEntity toEntity(Request request) {
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
