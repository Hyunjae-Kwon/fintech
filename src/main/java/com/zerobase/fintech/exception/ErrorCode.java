package com.zerobase.fintech.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  DUPLICATED_USERID(HttpStatus.CONFLICT.value(), "이미 존재하는 아이디입니다."),
  ALREADY_SIGNUP_USER(HttpStatus.CONFLICT.value(), "이미 가입한 회원 정보입니다."),
  USER_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "해당 유저가 존재하지 않습니다."),
  PASSWORD_INCORRECT(HttpStatus.CONFLICT.value(), "비밀번호가 일치하지 않습니다."),

  TOKEN_TIME_OUT(HttpStatus.CONFLICT.value(), "토큰이 만료되었습니다."),
  JWT_TOKEN_WRONG_TYPE(HttpStatus.UNAUTHORIZED.value(), "JWT 토큰 형식에 문제가 있습니다.");

  private final int statusCode;
  private final String description;
}
