package com.zerobase.fintech.account.dao;

import com.zerobase.fintech.account.entity.AccountEntity;
import com.zerobase.fintech.user.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, String> {
  boolean existsByAccountNumber(String accountNumber);

  void deleteByUserIdAndAccountNumber(UserEntity userId, String accountNumber);

  Optional<AccountEntity> findByAccountNumber(String accountNumber);

  Page<AccountEntity> findByUserId(UserEntity userId, Pageable pageable);
}
