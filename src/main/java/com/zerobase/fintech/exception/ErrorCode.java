package com.zerobase.fintech.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  DUPLICATED_USERID(HttpStatus.CONFLICT.value(), "이미 존재하는 아이디입니다."),
  ALREADY_SIGNUP_USER(HttpStatus.CONFLICT.value(), "이미 가입한 회원 정보입니다.");

  private final int statusCode;
  private final String description;
}
