package com.zerobase.fintech.account.entity;

import com.zerobase.fintech.user.entity.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Entity(name = "ACCOUNT")
public class AccountEntity {

  @Id
  private String accountNumber;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity userId;

  private int amount;
  private LocalDateTime createAt;
  private LocalDateTime latestAt;

  public void edit(int amount){
    if(amount != 0) {
      this.amount = amount;
    }

    this.latestAt = LocalDateTime.now();
  }
}
