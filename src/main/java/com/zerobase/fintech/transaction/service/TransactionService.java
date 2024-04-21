package com.zerobase.fintech.transaction.service;

import com.zerobase.fintech.account.dao.AccountRepository;
import com.zerobase.fintech.account.entity.AccountEntity;
import com.zerobase.fintech.exception.CustomException;
import com.zerobase.fintech.exception.ErrorCode;
import com.zerobase.fintech.transaction.dao.TransactionRepository;
import com.zerobase.fintech.transaction.entity.DepositForm;
import com.zerobase.fintech.transaction.entity.SearchForm;
import com.zerobase.fintech.transaction.entity.TransactionEntity;
import com.zerobase.fintech.transaction.entity.RemittanceForm;
import com.zerobase.fintech.transaction.entity.WithdrawForm;
import com.zerobase.fintech.user.entity.UserEntity;
import com.zerobase.fintech.util.PasswordUtils;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class TransactionService {

  private final TransactionRepository transactionRepository;
  private final AccountRepository accountRepository;

  public DepositForm depositTransaction(
      String accountNumber,
      String fromAccountNumber, DepositForm request) {

    AccountEntity account = accountRepository.findByAccountNumber(accountNumber)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

    if (fromAccountNumber != null) {
      String name = getNameByAccountNumber(fromAccountNumber);
      request.setTransactionName(name);
    } else {
      request.setTransactionName("ATM");
    }

    account.edit(account.getAmount() + request.getDeposit());

    accountRepository.save(account);

    TransactionEntity transactionEntity = transactionRepository.save(
        DepositForm.toEntity(request, account)
    );

    return DepositForm.fromDto(transactionEntity);
  }

  public WithdrawForm withdrawTransaction(String accountNumber,
      String toAccountNumber,
      WithdrawForm request, UserEntity userEntity) {

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

    if (request.getWithdraw() > account.getAmount()) {
      throw new CustomException(ErrorCode.LOW_AMOUNT);
    }

    if (toAccountNumber != null) {
      String name = getNameByAccountNumber(toAccountNumber);
      request.setTransactionName(name);
    } else {
      request.setTransactionName("ATM");
    }

    account.edit(account.getAmount() - request.getWithdraw());

    accountRepository.save(account);

    TransactionEntity withdraw = transactionRepository.save(
        WithdrawForm.toEntity(request, account)
    );

    return WithdrawForm.fromDto(withdraw);
  }

  public WithdrawForm remittanceTransaction(
      String accountNumber, String toAccountNumber,
      RemittanceForm request, UserEntity userEntity
  ) {
    DepositForm deposit = DepositForm.builder()
        .deposit(request.getAmount())
        .build();

    WithdrawForm withdraw = WithdrawForm.builder()
        .withdraw(request.getAmount())
        .userId(request.getUserId())
        .password(request.getPassword())
        .build();

    WithdrawForm remittance = withdrawTransaction(
        accountNumber, toAccountNumber, withdraw, userEntity
    );

    depositTransaction(toAccountNumber, accountNumber, deposit);

    return remittance;
  }

  public String getNameByAccountNumber(String accountNumber) {
    AccountEntity toAccount =
        accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(
                () -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

    return toAccount.getUserId().getName();
  }

  public Page<SearchForm> getTransactionHistory(String request,
      String orderBy, Integer page, String accountNumber,
      UserEntity userEntity) {

    AccountEntity account =
        accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(
                () -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

    if (!userEntity.getUserId().equals(account.getUserId().getUserId())) {
      throw new CustomException(ErrorCode.USER_NOT_MATCH);
    }

    Page<TransactionEntity> transactions;

    if (request == null) {
      transactions = switch (orderBy) {
        case "recent" -> transactionRepository
            .findByAccountNumberOrderByTransactionIdDesc(
                account, PageRequest.of(page, 10)
            );
        case "past" -> transactionRepository
            .findByAccountNumberOrderByTransactionId(
                account, PageRequest.of(page, 10)
            );
        default -> throw new IllegalArgumentException("Invalid orderBy value");
      };
    } else {
      transactions = switch (orderBy) {
        case "recent" -> switch (request) {
          case "deposit" -> transactionRepository
              .findByAccountNumberAndDepositGreaterThanOrderByTransactionIdDesc(
                  account, 0, PageRequest.of(page, 10)
              );
          case "withdraw" -> transactionRepository
              .findByAccountNumberAndDepositGreaterThanOrderByTransactionId(
                  account, 0, PageRequest.of(page, 10)
              );
          default ->
              throw new IllegalArgumentException("Invalid orderBy value");
        };

        case "past" -> switch (request) {
          case "deposit" -> transactionRepository
              .findByAccountNumberAndWithdrawGreaterThanOrderByTransactionIdDesc(
                  account, 0, PageRequest.of(page, 10)
              );
          case "withdraw" -> transactionRepository
              .findByAccountNumberAndWithdrawGreaterThanOrderByTransactionId(
                  account, 0, PageRequest.of(page, 10)
              );

          default ->
              throw new IllegalArgumentException("Invalid orderBy value");

        };

        default -> throw new IllegalArgumentException("Invalid orderBy value");
      };
    }

    if (transactions.getSize() == 0) {
      throw new CustomException(ErrorCode.NOT_HAVE_TRANSACTION);
    }

    return transactions.map(SearchForm::fromDto);
  }

  public Page<SearchForm> getRemittanceHistory(String atm, String orderBy,
      Integer page, String accountNumber, UserEntity userEntity) {

    AccountEntity account =
        accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(
                () -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

    if (!userEntity.getUserId().equals(account.getUserId().getUserId())) {
      throw new CustomException(ErrorCode.USER_NOT_MATCH);
    }

    Page<TransactionEntity> transactions = switch (orderBy) {
      case "recent" ->
          transactionRepository
              .findByAccountNumberAndTransactionNameNotOrderByTransactionIdDesc(
              account, atm, PageRequest.of(page, 10));
      case "past" ->
          transactionRepository
              .findByAccountNumberAndTransactionNameNotOrderByTransactionId(
              account, atm, PageRequest.of(page, 10));

      default -> throw new IllegalArgumentException("Invalid orderBy value");
    };

    if (transactions.getSize() == 0) {
      throw new CustomException(ErrorCode.NOT_HAVE_TRANSACTION);
    }

    return transactions.map(SearchForm::fromDto);
  }

  public Page<SearchForm> searchRemittance(String keyword,
      String orderBy, Integer page, String accountNumber,
      UserEntity userEntity) {

    AccountEntity account =
        accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(
                () -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

    if (!userEntity.getUserId().equals(account.getUserId().getUserId())) {
      throw new CustomException(ErrorCode.USER_NOT_MATCH);
    }

    Page<TransactionEntity> transactions = switch (orderBy) {
      case "recent" -> transactionRepository
          .findByAccountNumberAndTransactionNameContainingOrderByTransactionIdDesc(
              account, keyword, PageRequest.of(page, 10)
          );

      case "past" -> transactionRepository
          .findByAccountNumberAndTransactionNameContainingOrderByTransactionId(
              account, keyword, PageRequest.of(page, 10)
          );

      default -> throw new IllegalArgumentException("Invalid orderBy value");
    };

    if (transactions.getSize() == 0) {
      throw new CustomException(ErrorCode.NOT_HAVE_TRANSACTION);
    }

    return transactions.map(SearchForm::fromDto);
  }

  public Page<SearchForm> searchDateTransaction(String startDate,
      String endDate,
      String orderBy, Integer page, String accountNumber,
      UserEntity userEntity) {

    AccountEntity account =
        accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(
                () -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

    if (!userEntity.getUserId().equals(account.getUserId().getUserId())) {
      throw new CustomException(ErrorCode.USER_NOT_MATCH);
    }

    if (endDate == null) {
      endDate = startDate + " 23:59:59.000";
    } else {
      endDate = endDate + " 23:59:59.000";
    }

    startDate = startDate + " 00:00:00.000";
    DateTimeFormatter formatter = DateTimeFormatter
        .ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    LocalDateTime start = LocalDateTime.parse(startDate, formatter);
    LocalDateTime end = LocalDateTime.parse(endDate, formatter);

    Page<TransactionEntity> transactions = switch (orderBy) {
      case "recent" -> transactionRepository
          .findByAccountNumberAndCreateAtBetweenOrderByTransactionIdDesc(
              account, start, end, PageRequest.of(page, 10)
          );

      case "past" -> transactionRepository
          .findByAccountNumberAndCreateAtBetweenOrderByTransactionId(
              account, start, end, PageRequest.of(page, 10)
          );

      default -> throw new IllegalArgumentException("Invalid orderBy value");
    };

    if (transactions.getSize() == 0) {
      throw new CustomException(ErrorCode.NOT_HAVE_TRANSACTION);
    }

    return transactions.map(SearchForm::fromDto);
  }
}
