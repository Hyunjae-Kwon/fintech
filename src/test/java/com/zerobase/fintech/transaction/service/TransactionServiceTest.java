package com.zerobase.fintech.transaction.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.zerobase.fintech.account.service.AccountService;
import com.zerobase.fintech.exception.CustomException;
import com.zerobase.fintech.exception.ErrorCode;
import com.zerobase.fintech.transaction.entity.TransactionDto;
import com.zerobase.fintech.transaction.entity.TransactionForm;
import com.zerobase.fintech.user.entity.UserEntity;
import com.zerobase.fintech.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
@Transactional
class TransactionServiceTest {

  @Autowired
  AccountService accountService;

  @Autowired
  UserService userService;

  @Autowired
  TransactionService transactionService;

  @Test
  @DisplayName("Deposit_Transaction_Success")
  void depositTransaction() {
    // given
    String accountNumber = "9583268840115";
    String emptyAccountNumber = null;
    TransactionForm.Request request = TransactionForm.Request.builder()
        .amount(10000)
        .verify(true)
        .build();

    // when
    TransactionDto transactionDto =
        transactionService.depositTransaction(accountNumber,
            emptyAccountNumber, request);

    // then
    log.info("Transaction ID : {}", transactionDto.getTransactionId());
    log.info("Create At : {}", transactionDto.getCreateAt());
    assertEquals(transactionDto.getDeposit(), 10000);
    assertEquals(transactionDto.getTransactionName(), "ATM");
    assertTrue(transactionDto.isVerify());
    assertNotNull(transactionDto.getAccountNumber());
  }

  @Test
  @DisplayName("Deposit_Transaction_Fail : Account_Not_Found")
  void depositTransactionFail_AccountNotFound() {
    // given
    String accountNumber = "1234567890123";
    String emptyAccountNumber = null;
    TransactionForm.Request request = TransactionForm.Request.builder()
        .amount(10000)
        .transactionName("ATM")
        .verify(true)
        .build();

    // when
    try {
      transactionService.depositTransaction(accountNumber, emptyAccountNumber
          , request);

      // then
    } catch (CustomException e) {
      assertEquals(e.getErrorCode(), ErrorCode.ACCOUNT_NOT_FOUND);
      assertEquals(e.getErrorMessage(), ErrorCode.ACCOUNT_NOT_FOUND.getDescription());
    }
  }

  @Test
  @DisplayName("Deposit_Transaction_Fail : Least_Amount")
  void depositTransactionFail_LeastAmount() {
    // given
    String accountNumber = "9583268840115";
    String emptyAccountNumber = null;
    TransactionForm.Request request = TransactionForm.Request.builder()
        .amount(0)
        .transactionName("ATM")
        .verify(true)
        .build();

    // when
    try {
      transactionService.depositTransaction(accountNumber, emptyAccountNumber
          , request);

      // then
    } catch (CustomException e) {
      assertEquals(e.getErrorCode(), ErrorCode.LEAST_AMOUNT);
      assertEquals(e.getErrorMessage(), ErrorCode.LEAST_AMOUNT.getDescription());
    }
  }

  @Test
  @DisplayName("Withdraw_Transaction_Success")
  void withdrawTransaction() {
    // given
    String accountNumber = "9583268840115";
    String emptyAccountNumber = null;
    TransactionForm.Request request = TransactionForm.Request.builder()
        .amount(1000)
        .userId("test")
        .password("pw")
        .transactionName("ATM")
        .verify(true)
        .build();

    UserEntity userEntity = UserEntity.builder()
        .userId("test")
        .password("$2a$10$kgFE0NZY/FI0t13b8aQbQOnainXRhCDJrC0tn5UaM5/fQ2G4WiVSO")
        .build();

    // when
    TransactionDto transactionDto =
        transactionService.withdrawTransaction(accountNumber,
            emptyAccountNumber, request, userEntity);

    // then
    log.info("Transaction ID : {}", transactionDto.getTransactionId());
    log.info("Create At : {}", transactionDto.getCreateAt());
    assertEquals(transactionDto.getWithdraw(), 1000);
    assertEquals(transactionDto.getTransactionName(), "ATM");
    assertTrue(transactionDto.isVerify());
    assertNotNull(transactionDto.getAccountNumber());
  }

  @Test
  @DisplayName("Withdraw_Transaction_Fail : User_Not_Match")
  void withdrawTransactionFail_UserNotMatch() {
    // given
    String accountNumber = "9583268840115";
    String emptyAccountNumber = null;
    TransactionForm.Request request = TransactionForm.Request.builder()
        .amount(1000)
        .userId("test1")
        .password("pw")
        .transactionName("ATM")
        .verify(true)
        .build();

    UserEntity userEntity = UserEntity.builder()
        .userId("test")
        .password("$2a$10$kgFE0NZY/FI0t13b8aQbQOnainXRhCDJrC0tn5UaM5/fQ2G4WiVSO")
        .build();

    // when
    try {
      transactionService.withdrawTransaction(accountNumber,
          emptyAccountNumber, request, userEntity);

      // then
    } catch (CustomException e) {
      assertEquals(e.getErrorCode(), ErrorCode.USER_NOT_MATCH);
      assertEquals(e.getErrorMessage(), ErrorCode.USER_NOT_MATCH.getDescription());
    }
  }

  @Test
  @DisplayName("Withdraw_Transaction_Fail : Password_Incorrect")
  void withdrawTransactionFail_PasswordIncorrect() {
    // given
    String accountNumber = "9583268840115";
    String emptyAccountNumber = null;
    TransactionForm.Request request = TransactionForm.Request.builder()
        .amount(1000)
        .userId("test")
        .password("pw1")
        .transactionName("ATM")
        .verify(true)
        .build();

    UserEntity userEntity = UserEntity.builder()
        .userId("test")
        .password("$2a$10$kgFE0NZY/FI0t13b8aQbQOnainXRhCDJrC0tn5UaM5/fQ2G4WiVSO")
        .build();

    // when
    try {
      transactionService.withdrawTransaction(accountNumber,
          emptyAccountNumber, request, userEntity);

      // then
    } catch (CustomException e) {
      assertEquals(e.getErrorCode(), ErrorCode.PASSWORD_INCORRECT);
      assertEquals(e.getErrorMessage(), ErrorCode.PASSWORD_INCORRECT.getDescription());
    }
  }

  @Test
  @DisplayName("Withdraw_Transaction_Fail : Account_Not_Found")
  void withdrawTransactionFail_AccountNotFound() {
    // given
    String accountNumber = "0123456789012";
    String emptyAccountNumber = null;
    TransactionForm.Request request = TransactionForm.Request.builder()
        .amount(1000)
        .userId("test")
        .password("pw")
        .transactionName("ATM")
        .verify(true)
        .build();

    UserEntity userEntity = UserEntity.builder()
        .userId("test")
        .password("$2a$10$kgFE0NZY/FI0t13b8aQbQOnainXRhCDJrC0tn5UaM5/fQ2G4WiVSO")
        .build();

    // when
    try {
      transactionService.withdrawTransaction(accountNumber,
          emptyAccountNumber, request,
          userEntity);

      // then
    } catch (CustomException e) {
      assertEquals(e.getErrorCode(), ErrorCode.ACCOUNT_NOT_FOUND);
      assertEquals(e.getErrorMessage(), ErrorCode.ACCOUNT_NOT_FOUND.getDescription());
    }
  }

  @Test
  @DisplayName("Withdraw_Transaction_Fail : Not_Your_Account")
  void withdrawTransactionFail_NotYourAccount() {
    // given
    String accountNumber = "2361337411490";
    String emptyAccountNumber = null;
    TransactionForm.Request request = TransactionForm.Request.builder()
        .amount(1000)
        .userId("test")
        .password("pw")
        .transactionName("ATM")
        .verify(true)
        .build();

    UserEntity userEntity = UserEntity.builder()
        .userId("test")
        .password("$2a$10$kgFE0NZY/FI0t13b8aQbQOnainXRhCDJrC0tn5UaM5/fQ2G4WiVSO")
        .build();

    // when
    try {
      transactionService.withdrawTransaction(accountNumber,
          emptyAccountNumber, request,
          userEntity);

      // then
    } catch (CustomException e) {
      assertEquals(e.getErrorCode(), ErrorCode.NOT_YOUR_ACCOUNT);
      assertEquals(e.getErrorMessage(), ErrorCode.NOT_YOUR_ACCOUNT.getDescription());
    }
  }

  @Test
  @DisplayName("Withdraw_Transaction_Fail : Least_Amount")
  void withdrawTransactionFail_LeastAmount() {
    // given
    String accountNumber = "9583268840115";
    String emptyAccountNumber = null;
    TransactionForm.Request request = TransactionForm.Request.builder()
        .amount(0)
        .userId("test")
        .password("pw")
        .transactionName("ATM")
        .verify(true)
        .build();

    UserEntity userEntity = UserEntity.builder()
        .userId("test")
        .password("$2a$10$kgFE0NZY/FI0t13b8aQbQOnainXRhCDJrC0tn5UaM5/fQ2G4WiVSO")
        .build();

    // when
    try {
      transactionService.withdrawTransaction(accountNumber,
          emptyAccountNumber, request, userEntity);

      // then
    } catch (CustomException e) {
      assertEquals(e.getErrorCode(), ErrorCode.LEAST_AMOUNT);
      assertEquals(e.getErrorMessage(), ErrorCode.LEAST_AMOUNT.getDescription());
    }
  }

  @Test
  @DisplayName("Withdraw_Transaction_Fail : Low_Amount")
  void withdrawTransactionFail_LowAmount() {
    // given
    String accountNumber = "9583268840115";
    String emptyAccountNumber = null;
    TransactionForm.Request request = TransactionForm.Request.builder()
        .amount(100000)
        .userId("test")
        .password("pw")
        .transactionName("ATM")
        .verify(true)
        .build();

    UserEntity userEntity = UserEntity.builder()
        .userId("test")
        .password("$2a$10$kgFE0NZY/FI0t13b8aQbQOnainXRhCDJrC0tn5UaM5/fQ2G4WiVSO")
        .build();

    // when
    try {
      transactionService.withdrawTransaction(accountNumber,
          emptyAccountNumber, request,
          userEntity);

      // then
    } catch (CustomException e) {
      assertEquals(e.getErrorCode(), ErrorCode.LOW_AMOUNT);
      assertEquals(e.getErrorMessage(), ErrorCode.LOW_AMOUNT.getDescription());
    }
  }

  @Test
  @DisplayName("Transfer_Transaction_Success")
  void transferTransaction() {
    // given
    String accountNumber = "9583268840115";
    String toAccountNumber = "2361337411490";
    TransactionForm.Request request = TransactionForm.Request.builder()
        .amount(1000)
        .userId("test")
        .password("pw")
        .transactionName("ATM")
        .verify(true)
        .build();

    UserEntity userEntity = UserEntity.builder()
        .userId("test")
        .password("$2a$10$kgFE0NZY/FI0t13b8aQbQOnainXRhCDJrC0tn5UaM5/fQ2G4WiVSO")
        .build();

    // when
    TransactionDto withdrawDto =
        transactionService.withdrawTransaction(accountNumber, toAccountNumber
            , request, userEntity);

    transactionService.depositTransaction(toAccountNumber, accountNumber,
        request);

    // then
    log.info("Transaction ID : {}", withdrawDto.getTransactionId());
    log.info("Create At : {}", withdrawDto.getCreateAt());
    assertEquals(withdrawDto.getWithdraw(), 1000);
    assertEquals(withdrawDto.getTransactionName(), "test2");
    assertTrue(withdrawDto.isVerify());
    assertNotNull(withdrawDto.getAccountNumber());
  }
}