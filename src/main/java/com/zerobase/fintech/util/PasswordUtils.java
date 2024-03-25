package com.zerobase.fintech.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordUtils {
  // 비밀번호 해싱값 리턴
  public static String encPassword(String plainText){
    if(plainText == null || plainText.isEmpty()){
      return "";
    }
    return BCrypt.hashpw(plainText, BCrypt.gensalt());
  }

  // 요청된 비밀번호 입력값과 저장된 비밀번호의 해싱값이 맞는지 확인
  public static boolean equals(String plainText, String hashed){
    if(plainText == null || plainText.isEmpty()){
      return false;
    }
    if(hashed == null || hashed.isEmpty()){
      return false;
    }

    return BCrypt.checkpw(plainText, hashed);
  }

}
