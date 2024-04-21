package com.zerobase.fintech.transaction.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RemittanceForm {

  @Positive(message = "1원 이상의 금액을 입력하세요.")
  private int amount;

  @NotBlank(message = "아이디를 입력하세요.")
  private String userId;

  @NotBlank(message = "비밀번호를 입력하세요.")
  private String password;
}
