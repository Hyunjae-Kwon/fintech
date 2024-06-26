package com.zerobase.fintech.account.service;

import static org.junit.jupiter.api.Assertions.*;

import com.zerobase.fintech.account.entity.AccountDto;
import com.zerobase.fintech.account.entity.CreateForm;
import com.zerobase.fintech.account.entity.DeleteForm;
import com.zerobase.fintech.exception.CustomException;
import com.zerobase.fintech.exception.ErrorCode;
import com.zerobase.fintech.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

@Slf4j
@SpringBootTest
@Transactional
class AccountServiceTest {

  @Autowired
  AccountService accountService;

  @Autowired
  UserService userService;

  @Test
  @DisplayName("Create_Account_Success")
  void createAccount() {
    // given
    String userId = "test1";
    String accountNumber = "0123456789123";

    // when
    CreateForm createForm = accountService.createAccount(userId,
        accountNumber);

    // then
    log.info("Create At : {}", createForm.getCreateAt());
    assertEquals(createForm.getAmount(), 0);
    assertEquals(createForm.getAccountNumber(), "0123456789123");
    assertNotNull(createForm.getUserId());
  }

  @Test
  @DisplayName("Create_Account_Fail : User_Not_Found")
  void createAccountFail_UserNotFound() {
    // given
    String userId = "notUser";
    String accountNumber = "0123456789123";

    // when
    try {
      accountService.createAccount(userId, accountNumber);

      // then
    } catch (CustomException e) {
      assertEquals(e.getErrorCode(), ErrorCode.USER_NOT_FOUND);
      assertEquals(e.getErrorMessage(),
          ErrorCode.USER_NOT_FOUND.getDescription());
    }
  }

  @Test
  @DisplayName("Create_Account_Fail : Duplicated_AccountNumber")
  void createAccountFail_DuplicatedAccountNumber() {
    // given
    String userId = "test";
    String accountNumber = "9583268840115";

    // when
    try {
      accountService.createAccount(userId, accountNumber);

      // then
    } catch (CustomException e) {
      assertEquals(e.getErrorCode(), ErrorCode.DUPLICATED_ACCOUNTNUMBER);
      assertEquals(e.getErrorMessage(),
          ErrorCode.DUPLICATED_ACCOUNTNUMBER.getDescription());
    }
  }

  @Test
  @DisplayName("Delete_Account_Success")
  void deleteAccount() {
    // given
    DeleteForm request = DeleteForm.builder()
        .userId("test")
        .password("pw")
        .accountNumber("4489931284069")
        .build();

    // when
    accountService.deleteAccount(request);
    try {
      accountService.findAccountInfo(request.getAccountNumber());

      // then
    } catch (CustomException e) {
      assertEquals(e.getErrorCode(), ErrorCode.ACCOUNT_NOT_FOUND);
      assertEquals(e.getErrorMessage(),
          ErrorCode.ACCOUNT_NOT_FOUND.getDescription());
    }
  }

  @Test
  @DisplayName("Delete_Account_Fail : User_Not_Found")
  void deleteAccountFail_UserNotFound() {
    // given
    DeleteForm request = DeleteForm.builder()
        .userId("notUser")
        .password("pw")
        .accountNumber("0123456789123")
        .build();
    // when
    try {
      accountService.deleteAccount(request);
      // then
    } catch (CustomException e) {
      assertEquals(e.getErrorCode(), ErrorCode.USER_NOT_FOUND);
      assertEquals(e.getErrorMessage(),
          ErrorCode.USER_NOT_FOUND.getDescription());
    }
  }

  @Test
  @DisplayName("Delete_Account_Fail : Password_Incorrect")
  void deleteAccountFail_Password_Incorrect() {
    // given
    DeleteForm request = DeleteForm.builder()
        .userId("test2")
        .password("pw")
        .accountNumber("2361337411490")
        .build();

    // when
    try {
      accountService.deleteAccount(request);
      // then
    } catch (CustomException e) {
      assertEquals(e.getErrorCode(), ErrorCode.PASSWORD_INCORRECT);
      assertEquals(e.getErrorMessage(),
          ErrorCode.PASSWORD_INCORRECT.getDescription());
    }
  }

  @Test
  @DisplayName("Delete_Account_Fail : Not_Your_Account")
  void deleteAccountFail_NotYourAccount() {
    // given
    DeleteForm request = DeleteForm.builder()
        .userId("test2")
        .password("pw2")
        .accountNumber("9583268840115")
        .build();
    // when
    try {
      accountService.deleteAccount(request);
      // then
    } catch (CustomException e) {
      assertEquals(e.getErrorCode(), ErrorCode.NOT_YOUR_ACCOUNT);
      assertEquals(e.getErrorMessage(),
          ErrorCode.NOT_YOUR_ACCOUNT.getDescription());
    }
  }

  @Test
  @DisplayName("Delete_Account_Fail : Account_Not_Empty")
  void deleteAccountFail_AccountNotEmpty() {
    // given
    DeleteForm request = DeleteForm.builder()
        .userId("test2")
        .password("pw2")
        .accountNumber("2361337411490")
        .build();

    // when
    try {
      accountService.deleteAccount(request);
      // then
    } catch (CustomException e) {
      assertEquals(e.getErrorCode(), ErrorCode.ACCOUNT_NOT_EMPTY);
      assertEquals(e.getErrorMessage(),
          ErrorCode.ACCOUNT_NOT_EMPTY.getDescription());
    }
  }

  @Test
  @DisplayName("Delete_Account_Fail : Account_Not_Found")
  void deleteAccountFail_AccountNotFound() {
    // given
    DeleteForm request = DeleteForm.builder()
        .userId("test2")
        .password("pw2")
        .accountNumber("0123456789123")
        .build();

    // when
    try {
      accountService.deleteAccount(request);
      // then
    } catch (CustomException e) {
      assertEquals(e.getErrorCode(), ErrorCode.ACCOUNT_NOT_FOUND);
      assertEquals(e.getErrorMessage(),
          ErrorCode.ACCOUNT_NOT_FOUND.getDescription());
    }
  }

  @Test
  @DisplayName("Get_All_Account_Success")
  void getAllAccount() {
    // given
    String userId = "test";
    Integer page = 0;

    // when
    Page<AccountDto> accounts = accountService.getAllAccount(userId, page);

    // then
    assertNotNull(accounts.getContent());
    assertEquals(accounts.getTotalElements(), 2);
    assertEquals(accounts.getTotalPages(), 1);
    assertEquals(accounts.getSize(), 5);
  }
}