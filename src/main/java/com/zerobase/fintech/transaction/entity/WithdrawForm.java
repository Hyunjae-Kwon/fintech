package com.zerobase.fintech.transaction.entity;

import com.zerobase.fintech.account.entity.AccountEntity;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WithdrawForm {

  private String accountNumber;

  @Positive(message = "1원 이상의 금액을 입력하세요.")
  private int withdraw;

  @NotBlank(message = "아이디를 입력하세요.")
  private String userId;

  @NotBlank(message = "비밀번호를 입력하세요.")
  private String password;

  private int balance;
  private String transactionName;
  private boolean verify;


  private LocalDateTime createAt;

  @PrePersist
  public void onPrePersist() {
    String customLocalDateTimeFormat = LocalDateTime.now().format(
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    this.createAt = LocalDateTime.parse(customLocalDateTimeFormat,
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }

  public static TransactionEntity toEntity(WithdrawForm request,
      AccountEntity accountEntity) {
    return TransactionEntity.builder()
        .accountNumber(accountEntity)
        .withdraw(request.getWithdraw())
        .transactionName(request.getTransactionName())
        .verify(true)
        .build();
  }

  public static WithdrawForm fromDto(TransactionEntity transactionEntity) {
    return WithdrawForm.builder()
        .accountNumber(transactionEntity.getAccountNumber().getAccountNumber())
        .withdraw(transactionEntity.getWithdraw())
        .balance(transactionEntity.getAccountNumber().getAmount())
        .transactionName(transactionEntity.getTransactionName())
        .createAt(transactionEntity.getCreateAt())
        .build();
  }
}
