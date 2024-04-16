package com.zerobase.fintech.transaction.dao;

import com.zerobase.fintech.transaction.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends
    JpaRepository<TransactionEntity, String> {

}
