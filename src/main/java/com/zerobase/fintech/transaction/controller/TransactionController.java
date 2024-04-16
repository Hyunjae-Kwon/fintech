package com.zerobase.fintech.transaction.controller;

import com.zerobase.fintech.transaction.entity.TransactionDto;
import com.zerobase.fintech.transaction.entity.TransactionForm;
import com.zerobase.fintech.transaction.service.TransactionService;
import com.zerobase.fintech.user.entity.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "3. TRANSACTION")
public class TransactionController {

  private final TransactionService transactionService;

  @Operation(summary = "계좌 입금")
  @PostMapping("/transaction/deposit/{accountNumber}")
  public ResponseEntity<?> depositTransaction(
      @PathVariable(name = "accountNumber") String accountNumber,
      @RequestParam(required = false, value = "fromAccountNumber",
          defaultValue = "") String fromAccountNumber,
      @RequestBody TransactionForm.Request request
  ){
    TransactionDto depositTransaction =
        transactionService.depositTransaction(accountNumber, fromAccountNumber,
            request);

    return ResponseEntity.ok(TransactionForm.Response.fromDepositDto(depositTransaction));
  }

  @Operation(summary = "계좌 출금")
  @PostMapping("/transaction/withdraw/{accountNumber}")
  public ResponseEntity<?> withdrawTransaction(
      @PathVariable(name = "accountNumber") String accountNumber,
      @RequestParam(required = false, value = "toAccountNumber",
          defaultValue = "") String toAccountNumber,
      @RequestBody TransactionForm.Request request,
      @AuthenticationPrincipal UserEntity userEntity
  ){
    TransactionDto withdrawTransaction =
        transactionService.withdrawTransaction(accountNumber, toAccountNumber
            , request, userEntity);

    return ResponseEntity.ok(TransactionForm.Response.fromWithdrawDto(withdrawTransaction));
  }

  @Operation(summary = "계좌 송금")
  @PostMapping("/transaction/remittance/{accountNumber}&{toAccountNumber}")
  public ResponseEntity<?> remittanceTransaction(
      @PathVariable(name = "accountNumber") String accountNumber,
      @PathVariable(name = "toAccountNumber") String toAccountNumber,
      @RequestBody TransactionForm.Request request,
      @AuthenticationPrincipal UserEntity userEntity
  ) {

    TransactionDto remittanceTransaction =
        transactionService.remittanceTransaction(accountNumber,
            toAccountNumber, request, userEntity);

    return ResponseEntity.ok(
        TransactionForm.Response.fromWithdrawDto(remittanceTransaction));
  }
}
