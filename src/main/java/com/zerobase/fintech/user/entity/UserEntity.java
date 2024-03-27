package com.zerobase.fintech.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "USER")
public class UserEntity{

  @Id
  private String userId;
  private String password;

  private String name;
  private String email;
  private String birth;

  private LocalDateTime createAt;
}
