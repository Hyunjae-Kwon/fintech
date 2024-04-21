package com.zerobase.fintech.transaction.entity;

import com.zerobase.fintech.account.entity.AccountEntity;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepositForm {

  private String accountNumber;

  @Positive(message = "1원 이상의 금액을 입력하세요.")
  private int deposit;

  private String transactionName;
  private boolean verify;

  @CreatedDate
  private LocalDateTime createAt;

  @PrePersist
  public void onPrePersist() {
    String customLocalDateTimeFormat = LocalDateTime.now().format(
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    this.createAt = LocalDateTime.parse(customLocalDateTimeFormat,
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }

  public static TransactionEntity toEntity(DepositForm request,
      AccountEntity accountEntity) {
    return TransactionEntity.builder()
        .accountNumber(accountEntity)
        .deposit(request.getDeposit())
        .transactionName(request.getTransactionName())
        .verify(true)
        .build();
  }

  public static DepositForm fromDto(TransactionEntity transactionEntity) {
    return DepositForm.builder()
        .accountNumber(transactionEntity.getAccountNumber().getAccountNumber())
        .deposit(transactionEntity.getDeposit())
        .transactionName(transactionEntity.getTransactionName())
        .createAt(transactionEntity.getCreateAt())
        .build();
  }
}