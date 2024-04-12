package com.zerobase.fintech.transaction.service;

import static org.junit.jupiter.api.Assertions.*;

import com.zerobase.fintech.account.service.AccountService;
import com.zerobase.fintech.transaction.entity.DepositForm;
import com.zerobase.fintech.transaction.entity.TransactionDto;
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
    DepositForm.Request request = DepositForm.Request.builder()
        .deposit(10000)
        .transactionName("ATM")
        .verify(true)
        .build();

    // when
    TransactionDto transactionDto =
        transactionService.depositTransaction(accountNumber,
        request);

    // then
    log.info("Transaction ID : {}", transactionDto.getTransactionId());
    log.info("Create At : {}", transactionDto.getCreateAt());
    assertEquals(transactionDto.getDeposit(), 10000);
    assertEquals(transactionDto.getTransactionName(), "ATM");
    assertTrue(transactionDto.isVerify());
    assertNotNull(transactionDto.getAccountNumber());
  }

}