package com.zerobase.fintech.account.entity;

import com.zerobase.fintech.user.entity.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

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

  @CreatedDate
  private LocalDateTime createAt;
  private LocalDateTime latestAt;

  @PrePersist
  public void onPrePersist() {
    String customLocalDateTimeFormat = LocalDateTime.now().format(
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    this.createAt = LocalDateTime.parse(customLocalDateTimeFormat,
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }

  public void edit(int amount) {
    if (amount != 0) {
      this.amount = amount;
    }

    this.latestAt = LocalDateTime.now();
  }
}
