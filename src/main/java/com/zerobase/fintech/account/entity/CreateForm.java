package com.zerobase.fintech.account.entity;

import com.zerobase.fintech.user.entity.UserEntity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CreateForm {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Request {

    private UserEntity userId;

    public static AccountEntity toEntity(UserEntity user,
        String accountNumber) {
      return AccountEntity.builder()
          .accountNumber(accountNumber)
          .userId(user)
          .amount(0)
          .createAt(LocalDateTime.now())
          .build();
    }
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Response {

    private String accountNumber;
    private UserEntity userId;

    private int amount;
    private LocalDateTime createAt;

    public static Response fromDto(AccountDto accountDto) {
      return Response.builder()
          .accountNumber(accountDto.getAccountNumber())
          .userId(accountDto.getUserId())
          .amount(accountDto.getAmount())
          .createAt(accountDto.getCreateAt())
          .build();
    }
  }
}
