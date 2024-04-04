package com.zerobase.fintech.util;

import java.util.Random;

public class AccountUtils {

  private static final int LENGTH = 12;

  public static String createAccountNumber() {
    StringBuilder accountNumber = new StringBuilder();

    Random random = new Random();
    accountNumber.append(random.nextInt(9) + 1);

    for (int i = 0; i < LENGTH; i++) {
      accountNumber.append(random.nextInt(10));
    }

    return accountNumber.toString();
  }
}
