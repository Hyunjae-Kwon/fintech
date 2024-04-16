package com.zerobase.fintech.account.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class DeleteForm {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Request {

    private String userId;
    private String password;
    private String accountNumber;
  }
}
