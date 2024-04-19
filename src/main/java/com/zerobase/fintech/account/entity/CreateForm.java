package com.zerobase.fintech.account.entity;

import com.zerobase.fintech.user.entity.UserEntity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateForm {

  private String accountNumber;
  private String userId;
  private int amount;
  private LocalDateTime createAt;
  public static AccountEntity toEntity(UserEntity user,
      String accountNumber) {
    return AccountEntity.builder()
        .accountNumber(accountNumber)
        .userId(user)
        .amount(0)
        .createAt(LocalDateTime.now())
        .build();
  }

  public static CreateForm fromDto(AccountEntity accountEntity) {
    return CreateForm.builder()
        .accountNumber(accountEntity.getAccountNumber())
        .userId(accountEntity.getUserId().getUserId())
        .amount(accountEntity.getAmount())
        .createAt(accountEntity.getCreateAt())
        .build();
  }
}
