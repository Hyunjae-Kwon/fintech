package com.zerobase.fintech.transaction.controller;

import com.zerobase.fintech.transaction.entity.DepositForm;
import com.zerobase.fintech.transaction.entity.TransactionDto;
import com.zerobase.fintech.transaction.entity.RemittanceForm;
import com.zerobase.fintech.transaction.entity.WithdrawForm;
import com.zerobase.fintech.transaction.service.TransactionService;
import com.zerobase.fintech.user.entity.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
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
  @PostMapping("/transaction/deposit")
  public ResponseEntity<?> depositTransaction(
      @Pattern(regexp = "\\d{13}", message = "계좌번호는 13자리 숫자여야 합니다.")
      @RequestParam(value = "accountNumber") String accountNumber,
      @Validated @RequestBody DepositForm.Request request
  ) {
    TransactionDto depositTransaction =
        transactionService.depositTransaction(accountNumber, null,
            request);

    return ResponseEntity.ok(
        DepositForm.Response.fromDto(depositTransaction));
  }

  @Operation(summary = "계좌 출금")
  @PostMapping("/transaction/withdraw")
  public ResponseEntity<?> withdrawTransaction(
      @Pattern(regexp = "\\d{13}", message = "계좌번호는 13자리 숫자여야 합니다.")
      @RequestParam(value = "accountNumber") String accountNumber,
      @Validated @RequestBody WithdrawForm.Request request,
      @AuthenticationPrincipal UserEntity userEntity
  ) {
    TransactionDto withdrawTransaction =
        transactionService.withdrawTransaction(accountNumber, null
            , request, userEntity);

    return ResponseEntity.ok(
        WithdrawForm.Response.fromDto(withdrawTransaction));
  }

  @Operation(summary = "계좌 송금")
  @PostMapping("/transaction/remittance")
  public ResponseEntity<?> remittanceTransaction(
      @Pattern(regexp = "\\d{13}", message = "계좌번호는 13자리 숫자여야 합니다.")
      @RequestParam(value = "accountNumber") String accountNumber,
      @Pattern(regexp = "\\d{13}", message = "계좌번호는 13자리 숫자여야 합니다.")
      @RequestParam(value = "toAccountNumber") String toAccountNumber,
      @Validated @RequestBody RemittanceForm.Request request,
      @AuthenticationPrincipal UserEntity userEntity
  ) {

    TransactionDto remittanceTransaction =
        transactionService.remittanceTransaction(accountNumber,
            toAccountNumber, request, userEntity);

    return ResponseEntity.ok(
        RemittanceForm.Response.fromDto(remittanceTransaction));
  }
}
