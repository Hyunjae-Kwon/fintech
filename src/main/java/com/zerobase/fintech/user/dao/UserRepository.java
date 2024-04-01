package com.zerobase.fintech.user.dao;

import com.zerobase.fintech.user.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
  boolean existsByUserId(String userId);

  boolean existsByNameAndBirth(String name, String birth);

  Optional<UserEntity> findByUserId(String userId);
}
