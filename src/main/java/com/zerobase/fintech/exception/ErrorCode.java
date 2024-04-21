package com.zerobase.fintech.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  INVALID_INPUT(HttpStatus.BAD_REQUEST.value(), "필수 입력 값이 누락되었습니다."),
  INVALID_PATTERN(HttpStatus.BAD_REQUEST.value(), "형식에 맞게 입력 해야합니다."),

  DUPLICATED_USERID(HttpStatus.CONFLICT.value(), "이미 존재하는 아이디입니다."),
  ALREADY_SIGNUP_USER(HttpStatus.CONFLICT.value(), "이미 가입한 회원 정보입니다."),
  USER_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "해당 유저가 존재하지 않습니다."),
  USER_NOT_MATCH(HttpStatus.BAD_REQUEST.value(), "아이디가 일치하지 않습니다."),
  PASSWORD_INCORRECT(HttpStatus.CONFLICT.value(), "비밀번호가 일치하지 않습니다."),

  USER_NOT_LOGIN(HttpStatus.BAD_REQUEST.value(), "로그인이 필요합니다."),
  TOKEN_TIME_OUT(HttpStatus.CONFLICT.value(), "토큰이 만료되었습니다."),
  JWT_TOKEN_WRONG_TYPE(HttpStatus.UNAUTHORIZED.value(), "JWT 토큰 형식에 문제가 있습니다."),

  ACCOUNT_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "해당 계좌가 존재하지 않습니다."),
  NOT_HAVE_ACCOUNT(HttpStatus.BAD_REQUEST.value(), "소유하고 있는 계좌가 없습니다."),
  DUPLICATED_ACCOUNTNUMBER(HttpStatus.CONTINUE.value(), "계좌번호 생성 오류 : 중복"),

  NOT_YOUR_ACCOUNT(HttpStatus.BAD_REQUEST.value(), "본인 소유의 계좌가 아닙니다."),
  ACCOUNT_NOT_EMPTY(HttpStatus.BAD_REQUEST.value(), "계좌 잔액이 0이 아닙니다."),
  LOW_AMOUNT(HttpStatus.BAD_REQUEST.value(), "잔액이 부족합니다."),
  LEAST_AMOUNT(HttpStatus.BAD_REQUEST.value(), "0원 이하의 금액은 입금 및 출금을 할 수 없습니다."),
  NOT_HAVE_TRANSACTION(HttpStatus.BAD_REQUEST.value(), "거래 내역이 없습니다.");


  private final int statusCode;
  private final String description;
}
