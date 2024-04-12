package com.zerobase.fintech.transaction.controller;

import com.zerobase.fintech.transaction.entity.DepositForm;
import com.zerobase.fintech.transaction.entity.TransactionDto;
import com.zerobase.fintech.transaction.entity.WithdrawForm;
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
      @RequestBody DepositForm.Request request
  ){
    TransactionDto depositTransaction =
        transactionService.depositTransaction(accountNumber, request);

    return ResponseEntity.ok(DepositForm.Response.fromDto(depositTransaction));
  }

  @Operation(summary = "계좌 출금")
  @PostMapping("/transaction/withdraw/{accountNumber}")
  public ResponseEntity<?> withdrawTransaction(
      @PathVariable(name = "accountNumber") String accountNumber,
      @RequestBody WithdrawForm.Request request,
      @AuthenticationPrincipal UserEntity userEntity
  ){
    TransactionDto withdrawTransaction =
        transactionService.withdrawTransaction(accountNumber, request, userEntity);

    return ResponseEntity.ok(WithdrawForm.Response.fromDto(withdrawTransaction));
  }
}
