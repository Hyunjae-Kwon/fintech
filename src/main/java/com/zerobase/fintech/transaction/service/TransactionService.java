package com.zerobase.fintech.transaction.service;

import com.zerobase.fintech.account.dao.AccountRepository;
import com.zerobase.fintech.account.entity.AccountEntity;
import com.zerobase.fintech.exception.CustomException;
import com.zerobase.fintech.exception.ErrorCode;
import com.zerobase.fintech.transaction.dao.TransactionRepository;
import com.zerobase.fintech.transaction.entity.DepositForm;
import com.zerobase.fintech.transaction.entity.TransactionDto;
import com.zerobase.fintech.transaction.entity.TransactionEntity;
import com.zerobase.fintech.transaction.entity.WithdrawForm;
import com.zerobase.fintech.user.entity.UserEntity;
import com.zerobase.fintech.util.PasswordUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class TransactionService {

  private final TransactionRepository transactionRepository;
  private final AccountRepository accountRepository;

  public TransactionDto depositTransaction(String accountNumber,
      DepositForm.Request request) {

    AccountEntity account = accountRepository.findByAccountNumber(accountNumber)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

    if(request.getDeposit() <= 0) {
      throw new CustomException(ErrorCode.LEAST_AMOUNT);
    }

    request.setAccountNumber(account);

    account.edit(account.getAmount() + request.getDeposit());

    accountRepository.save(account);

    TransactionEntity deposit = transactionRepository.save(
        DepositForm.Request.toEntity(request)
    );

    return TransactionDto.from(deposit);
  }

  public TransactionDto withdrawTransaction(String accountNumber,
      WithdrawForm.Request request, UserEntity userEntity) {

    if(!request.getUserId().equals(userEntity.getUserId())) {
      throw new CustomException(ErrorCode.USER_NOT_MATCH);
    }

    if(!PasswordUtils.equals(request.getPassword(), userEntity.getPassword())) {
      throw new CustomException(ErrorCode.PASSWORD_INCORRECT);
    }

    AccountEntity account = accountRepository.findByAccountNumber(accountNumber)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

    if(!account.getUserId().getUserId().equals(request.getUserId())) {
      throw new CustomException(ErrorCode.NOT_YOUR_ACCOUNT);
    }

    if(request.getWithdraw() <= 0) {
      throw new CustomException(ErrorCode.LEAST_AMOUNT);
    }

    request.setAccountNumber(account);

    account.edit(account.getAmount() - request.getWithdraw());

    accountRepository.save(account);

    TransactionEntity withdraw = transactionRepository.save(
        WithdrawForm.Request.toEntity(request)
    );

    return TransactionDto.from(withdraw);
  }
}
