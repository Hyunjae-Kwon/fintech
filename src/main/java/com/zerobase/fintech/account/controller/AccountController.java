package com.zerobase.fintech.account.controller;

import com.zerobase.fintech.account.entity.AccountDto;
import com.zerobase.fintech.account.entity.CreateForm;
import com.zerobase.fintech.account.entity.DeleteForm;
import com.zerobase.fintech.account.service.AccountService;
import com.zerobase.fintech.exception.CustomException;
import com.zerobase.fintech.exception.ErrorCode;
import com.zerobase.fintech.user.entity.UserEntity;
import com.zerobase.fintech.util.AccountUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
@Tag(name = "2. ACCOUNT")
public class AccountController {

  private final AccountService accountService;

  @Operation(summary = "계좌 생성")
  @PostMapping("/account/create/{userId}")
  public ResponseEntity<?> createAccount(
      @PathVariable(name = "userId") String userId,
      @AuthenticationPrincipal UserEntity userEntity
  ) {
    if (!userId.equals(userEntity.getUserId())) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }

    String accountNumber = AccountUtils.createAccountNumber();

    CreateForm createAccount = accountService.createAccount(userId,
        accountNumber);

    return ResponseEntity.ok(createAccount);
  }

  @Operation(summary = "계좌 삭제")
  @PostMapping("/account/delete")
  public ResponseEntity<?> deleteAccount(
      @Validated @RequestBody DeleteForm request,
      @AuthenticationPrincipal UserEntity userEntity
  ) {
    if (!request.getUserId().equals(userEntity.getUserId())) {
      throw new CustomException(ErrorCode.USER_NOT_MATCH);
    }

    accountService.deleteAccount(request);

    return ResponseEntity.ok("Account Delete Success");
  }

  @Operation(summary = "계좌 조회")
  @GetMapping("/account/all")
  public ResponseEntity<?> getAllAccount(
      @RequestParam(value = "p", defaultValue = "0") Integer page,
      @AuthenticationPrincipal UserEntity userEntity
  ) {
    Page<AccountDto> accounts =
        accountService.getAllAccount(userEntity.getUserId(), page);

    return ResponseEntity.ok(accounts);
  }
}
