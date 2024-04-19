package com.zerobase.fintech.account.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountDto {

  private String accountNumber;
  private String userId;
  private int amount;
  private LocalDateTime createAt;

  public static AccountDto from(AccountEntity accountEntity) {
    return AccountDto.builder()
        .accountNumber(accountEntity.getAccountNumber())
        .userId(accountEntity.getUserId().getUserId())
        .amount(accountEntity.getAmount())
        .createAt(accountEntity.getCreateAt())
        .build();
  }
}
