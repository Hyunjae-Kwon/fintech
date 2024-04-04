package com.zerobase.fintech.account.service;

import com.zerobase.fintech.account.dao.AccountRepository;
import com.zerobase.fintech.account.entity.AccountDto;
import com.zerobase.fintech.account.entity.AccountEntity;
import com.zerobase.fintech.account.entity.CreateForm;
import com.zerobase.fintech.account.entity.DeleteForm;
import com.zerobase.fintech.exception.CustomException;
import com.zerobase.fintech.exception.ErrorCode;
import com.zerobase.fintech.user.dao.UserRepository;
import com.zerobase.fintech.user.entity.UserEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AccountService {

  private final AccountRepository accountRepository;
  private final UserRepository userRepository;

  public AccountDto createAccount(String userId, String accountNumber) {
    UserEntity user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    if(accountRepository.existsByAccountNumber(accountNumber)) {
      throw new CustomException(ErrorCode.DUPLICATED_ACCOUNTNUMBER);
    }

    AccountEntity createAccount = accountRepository.save(
        CreateForm.Request.toEntity(user, accountNumber)
    );

    return AccountDto.from(createAccount);
  }

  public void deleteAccount(DeleteForm.Request request) {

    UserEntity user = userRepository.findByUserId(request.getUserId())
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    AccountDto account = this.findAccountInfo(request.getAccountNumber());

    if(!request.getUserId().equals(account.getUserId().getUserId())) {
      throw new CustomException(ErrorCode.NOT_YOUR_ACCOUNT);
    }

    if(account.getAmount() != 0) {
      throw new CustomException(ErrorCode.ACCOUNT_NOT_EMPTY);
    }

    accountRepository.deleteByUserIdAndAccountNumber(
        user, request.getAccountNumber());

    log.info("Delete AccountNumber Success => Account Info : {}, {}",
        user.getUserId(), request.getAccountNumber());
  }

  public AccountDto findAccountInfo(String accountNumber) {
    AccountEntity account = accountRepository.findByAccountNumber(accountNumber)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

    return AccountDto.from(account);
  }
}