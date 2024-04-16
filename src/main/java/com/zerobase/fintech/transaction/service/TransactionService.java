package com.zerobase.fintech.transaction.service;

import com.zerobase.fintech.account.dao.AccountRepository;
import com.zerobase.fintech.account.entity.AccountEntity;
import com.zerobase.fintech.exception.CustomException;
import com.zerobase.fintech.exception.ErrorCode;
import com.zerobase.fintech.transaction.dao.TransactionRepository;
import com.zerobase.fintech.transaction.entity.TransactionDto;
import com.zerobase.fintech.transaction.entity.TransactionEntity;
import com.zerobase.fintech.transaction.entity.TransactionForm;
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
      String fromAccountNumber, TransactionForm.Request request) {

    AccountEntity account = accountRepository.findByAccountNumber(accountNumber)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

    if (request.getAmount() <= 0) {
      throw new CustomException(ErrorCode.LEAST_AMOUNT);
    }

    if (fromAccountNumber.isBlank()) {
      request.setTransactionName("ATM");
    } else {
      AccountEntity fromAccount = accountRepository.findByAccountNumber(
              fromAccountNumber)
          .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
      String name = fromAccount.getUserId().getName();
      request.setTransactionName(name);
    }

    request.setToAccountNumber(account);

    account.edit(account.getAmount() + request.getAmount());

    accountRepository.save(account);

    TransactionEntity deposit = transactionRepository.save(
        TransactionForm.Request.toDepositEntity(request)
    );

    return TransactionDto.from(deposit);
  }

  public TransactionDto withdrawTransaction(String accountNumber,
      String toAccountNumber,
      TransactionForm.Request request, UserEntity userEntity) {

    if (userEntity == null) {
      throw new CustomException(ErrorCode.USER_NOT_LOGIN);
    }

    if (!request.getUserId().equals(userEntity.getUserId())) {
      throw new CustomException(ErrorCode.USER_NOT_MATCH);
    }

    if (!PasswordUtils.equals(request.getPassword(),
        userEntity.getPassword())) {
      throw new CustomException(ErrorCode.PASSWORD_INCORRECT);
    }

    AccountEntity account = accountRepository.findByAccountNumber(accountNumber)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

    if (!account.getUserId().getUserId().equals(request.getUserId())) {
      throw new CustomException(ErrorCode.NOT_YOUR_ACCOUNT);
    }

    if (request.getAmount() <= 0) {
      throw new CustomException(ErrorCode.LEAST_AMOUNT);
    }

    if (request.getAmount() > account.getAmount()) {
      throw new CustomException(ErrorCode.LOW_AMOUNT);
    }

    if (toAccountNumber.isBlank()) {
      request.setTransactionName("ATM");
    } else {
      AccountEntity toAccount =
          accountRepository.findByAccountNumber(toAccountNumber)
              .orElseThrow(
                  () -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
      String name = toAccount.getUserId().getName();
      request.setTransactionName(name);
    }

    request.setAccountNumber(account);

    account.edit(account.getAmount() - request.getAmount());

    accountRepository.save(account);

    TransactionEntity withdraw = transactionRepository.save(
        TransactionForm.Request.toWithdrawEntity(request)
    );

    return TransactionDto.from(withdraw);
  }

  public TransactionDto remittanceTransaction(
      String accountNumber, String toAccountNumber,
      TransactionForm.Request request, UserEntity userEntity
  ) {
    TransactionDto remittance = withdrawTransaction(
        accountNumber, toAccountNumber, request, userEntity
    );

    depositTransaction(toAccountNumber, accountNumber, request);

    return remittance;
  }
}
