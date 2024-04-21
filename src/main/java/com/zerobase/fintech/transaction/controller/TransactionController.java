package com.zerobase.fintech.transaction.controller;

import com.zerobase.fintech.transaction.entity.DepositForm;
import com.zerobase.fintech.transaction.entity.RemittanceForm;
import com.zerobase.fintech.transaction.entity.SearchForm;
import com.zerobase.fintech.transaction.entity.WithdrawForm;
import com.zerobase.fintech.transaction.service.TransactionService;
import com.zerobase.fintech.user.entity.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
  @PostMapping("/transaction/deposit")
  public ResponseEntity<?> depositTransaction(
      @Pattern(regexp = "\\d{13}", message = "계좌번호는 13자리 숫자여야 합니다.")
      @RequestParam(value = "accountNumber") String accountNumber,
      @Validated @RequestBody DepositForm request
  ) {
    DepositForm depositTransaction =
        transactionService.depositTransaction(
            accountNumber, null, request);

    return ResponseEntity.ok(depositTransaction);
  }

  @Operation(summary = "계좌 출금")
  @PostMapping("/transaction/withdraw")
  public ResponseEntity<?> withdrawTransaction(
      @Pattern(regexp = "\\d{13}", message = "계좌번호는 13자리 숫자여야 합니다.")
      @RequestParam(value = "accountNumber") String accountNumber,
      @Validated @RequestBody WithdrawForm request,
      @AuthenticationPrincipal UserEntity userEntity
  ) {
    WithdrawForm withdrawTransaction =
        transactionService.withdrawTransaction(
            accountNumber, null, request, userEntity);

    return ResponseEntity.ok(withdrawTransaction);
  }

  @Operation(summary = "계좌 송금")
  @PostMapping("/transaction/remittance")
  public ResponseEntity<?> remittanceTransaction(
      @Pattern(regexp = "\\d{13}", message = "계좌번호는 13자리 숫자여야 합니다.")
      @RequestParam(value = "accountNumber") String accountNumber,
      @Pattern(regexp = "\\d{13}", message = "계좌번호는 13자리 숫자여야 합니다.")
      @RequestParam(value = "toAccountNumber") String toAccountNumber,
      @Validated @RequestBody RemittanceForm request,
      @AuthenticationPrincipal UserEntity userEntity
  ) {

    WithdrawForm remittanceTransaction =
        transactionService.remittanceTransaction(accountNumber,
            toAccountNumber, request, userEntity);

    return ResponseEntity.ok(remittanceTransaction);
  }

  @Operation(summary = "계좌 입출금 내역 조회")
  @GetMapping("/transaction/history/{accountNumber}")
  public ResponseEntity<?> getTransactionHistory(
      @RequestParam(required = false, value = "request") String request,
      @RequestParam(value = "orderBy", defaultValue = "recent") String orderBy,
      @RequestParam(value = "p", defaultValue = "0") Integer page,
      @Pattern(regexp = "\\d{13}", message = "계좌번호는 13자리 숫자여야 합니다.")
      @PathVariable(name = "accountNumber") String accountNumber,
      @AuthenticationPrincipal UserEntity userEntity
  ) {

    Page<SearchForm> transactions =
        transactionService.getTransactionHistory(request, orderBy,
            page, accountNumber, userEntity);

    return ResponseEntity.ok(transactions);
  }

  @Operation(summary = "송금 내역 조회")
  @GetMapping("/transaction/remittance/{accountNumber}")
  public ResponseEntity<?> getRemittanceHistory(
      @RequestParam(value = "keyword", defaultValue = "ATM") String keyword,
      @RequestParam(value = "orderBy", defaultValue = "recent") String orderBy,
      @RequestParam(value = "p", defaultValue = "0") Integer page,
      @Pattern(regexp = "\\d{13}", message = "계좌번호는 13자리 숫자여야 합니다.")
      @PathVariable(name = "accountNumber") String accountNumber,
      @AuthenticationPrincipal UserEntity userEntity
  ) {

    Page<SearchForm> transactions =
        transactionService.getRemittanceHistory(keyword, orderBy,
            page, accountNumber, userEntity);

    return ResponseEntity.ok(transactions);
  }

  @Operation(summary = "거래 내역 검색 - 송금자")
  @GetMapping("/transaction/searchKeyword/{accountNumber}")
  public ResponseEntity<?> searchRemittance(
      @RequestParam(value = "keyword") String keyword,
      @RequestParam(value = "orderBy", defaultValue = "recent") String orderBy,
      @RequestParam(value = "p", defaultValue = "0") Integer page,
      @Pattern(regexp = "\\d{13}", message = "계좌번호는 13자리 숫자여야 합니다.")
      @PathVariable(name = "accountNumber") String accountNumber,
      @AuthenticationPrincipal UserEntity userEntity
  ) {

    Page<SearchForm> transactions =
        transactionService.searchRemittance(keyword, orderBy,
            page, accountNumber, userEntity);

    return ResponseEntity.ok(transactions);
  }

  @Operation(summary = "거래 내역 검색 - 날짜")
  @GetMapping("/transaction/searchDate/{accountNumber}")
  public ResponseEntity<?> searchDateTransaction(
      @DateTimeFormat(pattern = "yyyy-MM-dd")
      @RequestParam(value = "startDate") String startDate,
      @DateTimeFormat(pattern = "yyyy-MM-dd")
      @RequestParam(required = false, value = "endDate") String endDate,
      @RequestParam(value = "orderBy", defaultValue = "recent") String orderBy,
      @RequestParam(value = "p", defaultValue = "0") Integer page,
      @Pattern(regexp = "\\d{13}", message = "계좌번호는 13자리 숫자여야 합니다.")
      @PathVariable(name = "accountNumber") String accountNumber,
      @AuthenticationPrincipal UserEntity userEntity
  ) {

    Page<SearchForm> transactions =
        transactionService.searchDateTransaction(startDate, endDate, orderBy,
            page, accountNumber, userEntity);

    return ResponseEntity.ok(transactions);
  }
}
