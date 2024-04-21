package com.zerobase.fintech.transaction.dao;

import com.zerobase.fintech.account.entity.AccountEntity;
import com.zerobase.fintech.transaction.entity.TransactionEntity;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends
    JpaRepository<TransactionEntity, String> {

  Page<TransactionEntity> findByAccountNumberOrderByTransactionIdDesc(
      AccountEntity accountNumber, Pageable pageable);

  Page<TransactionEntity> findByAccountNumberOrderByTransactionId(
      AccountEntity accountNumber, Pageable pageable);

  Page<TransactionEntity> findByAccountNumberAndDepositGreaterThanOrderByTransactionIdDesc(
      AccountEntity accountNumber, int deposit, Pageable pageable);

  Page<TransactionEntity> findByAccountNumberAndDepositGreaterThanOrderByTransactionId(
      AccountEntity accountNumber, int deposit, Pageable pageable);

  Page<TransactionEntity> findByAccountNumberAndWithdrawGreaterThanOrderByTransactionIdDesc(
      AccountEntity accountNumber, int withdraw, Pageable pageable);

  Page<TransactionEntity> findByAccountNumberAndWithdrawGreaterThanOrderByTransactionId(
      AccountEntity accountNumber, int withdraw, Pageable pageable);

  Page<TransactionEntity> findByAccountNumberAndTransactionNameNotOrderByTransactionIdDesc(
      AccountEntity accountNumber, String atm, Pageable pageable);

  Page<TransactionEntity> findByAccountNumberAndTransactionNameNotOrderByTransactionId(
      AccountEntity accountNumber, String atm, Pageable pageable);

  Page<TransactionEntity> findByAccountNumberAndTransactionNameContainingOrderByTransactionIdDesc(
      AccountEntity accountNumber, String keyword, Pageable pageable);

  Page<TransactionEntity> findByAccountNumberAndTransactionNameContainingOrderByTransactionId(
      AccountEntity accountNumber, String keyword, Pageable pageable);

  Page<TransactionEntity> findByAccountNumberAndCreateAtBetweenOrderByTransactionIdDesc(
      AccountEntity accountNumber, LocalDateTime startDate,
      LocalDateTime endDate, Pageable pageable);

  Page<TransactionEntity> findByAccountNumberAndCreateAtBetweenOrderByTransactionId(
      AccountEntity accountNumber, LocalDateTime startDate,
      LocalDateTime endDate, Pageable pageable);
}