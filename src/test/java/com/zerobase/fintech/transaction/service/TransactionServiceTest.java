package com.zerobase.fintech.transaction.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.zerobase.fintech.account.service.AccountService;
import com.zerobase.fintech.exception.CustomException;
import com.zerobase.fintech.exception.ErrorCode;
import com.zerobase.fintech.transaction.entity.DepositForm;
import com.zerobase.fintech.transaction.entity.RemittanceForm;
import com.zerobase.fintech.transaction.entity.WithdrawForm;
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
    DepositForm request = DepositForm.builder()
        .deposit(10000)
        .build();

    // when
    DepositForm depositForm =
        transactionService.depositTransaction(accountNumber,
            emptyAccountNumber, request);

    // then
    log.info("Create At : {}", depositForm.getCreateAt());
    assertEquals(depositForm.getDeposit(), 10000);
    assertEquals(depositForm.getTransactionName(), "ATM");
    assertEquals(depositForm.getAccountNumber(), "9583268840115");
  }

  @Test
  @DisplayName("Deposit_Transaction_Fail : Account_Not_Found")
  void depositTransactionFail_AccountNotFound() {
    // given
    String accountNumber = "1234567890123";
    String emptyAccountNumber = null;
    DepositForm request = DepositForm.builder()
        .deposit(10000)
        .transactionName("ATM")
        .build();

    // when
    try {
      transactionService.depositTransaction(accountNumber, emptyAccountNumber
          , request);

      // then
    } catch (CustomException e) {
      assertEquals(e.getErrorCode(), ErrorCode.ACCOUNT_NOT_FOUND);
      assertEquals(e.getErrorMessage(),
          ErrorCode.ACCOUNT_NOT_FOUND.getDescription());
    }
  }

  @Test
  @DisplayName("Deposit_Transaction_Fail : Least_Amount")
  void depositTransactionFail_LeastAmount() {
    // given
    String accountNumber = "9583268840115";
    String emptyAccountNumber = null;
    DepositForm request = DepositForm.builder()
        .deposit(0)
        .transactionName("ATM")
        .build();

    // when
    try {
      transactionService.depositTransaction(accountNumber, emptyAccountNumber
          , request);

      // then
    } catch (CustomException e) {
      assertEquals(e.getErrorCode(), ErrorCode.LEAST_AMOUNT);
      assertEquals(e.getErrorMessage(),
          ErrorCode.LEAST_AMOUNT.getDescription());
    }
  }

  @Test
  @DisplayName("Withdraw_Transaction_Success")
  void withdrawTransaction() {
    // given
    String accountNumber = "9583268840115";
    String emptyAccountNumber = null;
    WithdrawForm request = WithdrawForm.builder()
        .withdraw(1000)
        .userId("test")
        .password("pw")
        .transactionName("ATM")
        .build();

    UserEntity userEntity = UserEntity.builder()
        .userId("test")
        .password(
            "$2a$10$kgFE0NZY/FI0t13b8aQbQOnainXRhCDJrC0tn5UaM5/fQ2G4WiVSO")
        .build();

    // when
    WithdrawForm withdrawForm =
        transactionService.withdrawTransaction(accountNumber,
            emptyAccountNumber, request, userEntity);

    // then
    log.info("Create At : {}", withdrawForm.getCreateAt());
    assertEquals(withdrawForm.getAccountNumber(), "9583268840115");
    assertEquals(withdrawForm.getWithdraw(), 1000);
    assertEquals(withdrawForm.getBalance(), 34000);
    assertEquals(withdrawForm.getTransactionName(), "ATM");

  }

  @Test
  @DisplayName("Withdraw_Transaction_Fail : User_Not_Match")
  void withdrawTransactionFail_UserNotMatch() {
    // given
    String accountNumber = "9583268840115";
    String emptyAccountNumber = null;
    WithdrawForm request = WithdrawForm.builder()
        .withdraw(1000)
        .userId("test1")
        .password("pw")
        .transactionName("ATM")
        .build();

    UserEntity userEntity = UserEntity.builder()
        .userId("test")
        .password(
            "$2a$10$kgFE0NZY/FI0t13b8aQbQOnainXRhCDJrC0tn5UaM5/fQ2G4WiVSO")
        .build();

    // when
    try {
      transactionService.withdrawTransaction(accountNumber,
          emptyAccountNumber, request, userEntity);

      // then
    } catch (CustomException e) {
      assertEquals(e.getErrorCode(), ErrorCode.USER_NOT_MATCH);
      assertEquals(e.getErrorMessage(),
          ErrorCode.USER_NOT_MATCH.getDescription());
    }
  }

  @Test
  @DisplayName("Withdraw_Transaction_Fail : Password_Incorrect")
  void withdrawTransactionFail_PasswordIncorrect() {
    // given
    String accountNumber = "9583268840115";
    String emptyAccountNumber = null;
    WithdrawForm request = WithdrawForm.builder()
        .withdraw(1000)
        .userId("test")
        .password("pw1")
        .transactionName("ATM")
        .build();

    UserEntity userEntity = UserEntity.builder()
        .userId("test")
        .password(
            "$2a$10$kgFE0NZY/FI0t13b8aQbQOnainXRhCDJrC0tn5UaM5/fQ2G4WiVSO")
        .build();

    // when
    try {
      transactionService.withdrawTransaction(accountNumber,
          emptyAccountNumber, request, userEntity);

      // then
    } catch (CustomException e) {
      assertEquals(e.getErrorCode(), ErrorCode.PASSWORD_INCORRECT);
      assertEquals(e.getErrorMessage(),
          ErrorCode.PASSWORD_INCORRECT.getDescription());
    }
  }

  @Test
  @DisplayName("Withdraw_Transaction_Fail : Account_Not_Found")
  void withdrawTransactionFail_AccountNotFound() {
    // given
    String accountNumber = "0123456789012";
    String emptyAccountNumber = null;
    WithdrawForm request = WithdrawForm.builder()
        .withdraw(1000)
        .userId("test")
        .password("pw")
        .transactionName("ATM")
        .build();

    UserEntity userEntity = UserEntity.builder()
        .userId("test")
        .password(
            "$2a$10$kgFE0NZY/FI0t13b8aQbQOnainXRhCDJrC0tn5UaM5/fQ2G4WiVSO")
        .build();

    // when
    try {
      transactionService.withdrawTransaction(accountNumber,
          emptyAccountNumber, request,
          userEntity);

      // then
    } catch (CustomException e) {
      assertEquals(e.getErrorCode(), ErrorCode.ACCOUNT_NOT_FOUND);
      assertEquals(e.getErrorMessage(),
          ErrorCode.ACCOUNT_NOT_FOUND.getDescription());
    }
  }

  @Test
  @DisplayName("Withdraw_Transaction_Fail : Not_Your_Account")
  void withdrawTransactionFail_NotYourAccount() {
    // given
    String accountNumber = "2361337411490";
    String emptyAccountNumber = null;
    WithdrawForm request = WithdrawForm.builder()
        .withdraw(1000)
        .userId("test")
        .password("pw")
        .transactionName("ATM")
        .build();

    UserEntity userEntity = UserEntity.builder()
        .userId("test")
        .password(
            "$2a$10$kgFE0NZY/FI0t13b8aQbQOnainXRhCDJrC0tn5UaM5/fQ2G4WiVSO")
        .build();

    // when
    try {
      transactionService.withdrawTransaction(accountNumber,
          emptyAccountNumber, request,
          userEntity);

      // then
    } catch (CustomException e) {
      assertEquals(e.getErrorCode(), ErrorCode.NOT_YOUR_ACCOUNT);
      assertEquals(e.getErrorMessage(),
          ErrorCode.NOT_YOUR_ACCOUNT.getDescription());
    }
  }

  @Test
  @DisplayName("Withdraw_Transaction_Fail : Least_Amount")
  void withdrawTransactionFail_LeastAmount() {
    // given
    String accountNumber = "9583268840115";
    String emptyAccountNumber = null;
    WithdrawForm request = WithdrawForm.builder()
        .withdraw(0)
        .userId("test")
        .password("pw")
        .transactionName("ATM")
        .build();

    UserEntity userEntity = UserEntity.builder()
        .userId("test")
        .password(
            "$2a$10$kgFE0NZY/FI0t13b8aQbQOnainXRhCDJrC0tn5UaM5/fQ2G4WiVSO")
        .build();

    // when
    try {
      transactionService.withdrawTransaction(accountNumber,
          emptyAccountNumber, request, userEntity);

      // then
    } catch (CustomException e) {
      assertEquals(e.getErrorCode(), ErrorCode.LEAST_AMOUNT);
      assertEquals(e.getErrorMessage(),
          ErrorCode.LEAST_AMOUNT.getDescription());
    }
  }

  @Test
  @DisplayName("Withdraw_Transaction_Fail : Low_Amount")
  void withdrawTransactionFail_LowAmount() {
    // given
    String accountNumber = "9583268840115";
    String emptyAccountNumber = null;
    WithdrawForm request = WithdrawForm.builder()
        .withdraw(100000)
        .userId("test")
        .password("pw")
        .transactionName("ATM")
        .build();

    UserEntity userEntity = UserEntity.builder()
        .userId("test")
        .password(
            "$2a$10$kgFE0NZY/FI0t13b8aQbQOnainXRhCDJrC0tn5UaM5/fQ2G4WiVSO")
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
  @DisplayName("Remittance_Transaction_Success")
  void remittanceTransactionTest() {
    // given
    String accountNumber = "9583268840115";
    String toAccountNumber = "2361337411490";
    RemittanceForm request = RemittanceForm.builder()
        .amount(1000)
        .userId("test")
        .password("pw")
        .build();

    UserEntity userEntity = UserEntity.builder()
        .userId("test")
        .password(
            "$2a$10$kgFE0NZY/FI0t13b8aQbQOnainXRhCDJrC0tn5UaM5/fQ2G4WiVSO")
        .build();

    // when
    WithdrawForm remittanceTransaction =
        transactionService.remittanceTransaction(accountNumber, toAccountNumber
            , request, userEntity);

    // then
    log.info("Create At : {}", remittanceTransaction.getCreateAt());
    assertEquals(remittanceTransaction.getAccountNumber(), "9583268840115");
    assertEquals(remittanceTransaction.getWithdraw(), 1000);
    assertEquals(remittanceTransaction.getBalance(), 34000);
    assertEquals(remittanceTransaction.getTransactionName(), "테스트2");
  }
}