package com.zerobase.fintech.transaction.entity;

import com.zerobase.fintech.account.entity.AccountEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Entity(name = "TRANSACTION")
public class TransactionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int transactionId;

  @ManyToOne
  @JoinColumn(name = "account_number")
  private AccountEntity accountNumber;

  private int deposit;
  private int withdraw;
  private String transactionName;
  private boolean verify;

  @CreatedDate
  private LocalDateTime createAt;

  @PrePersist
  public void onPrePersist() {
    String customLocalDateTimeFormat = LocalDateTime.now().format(
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    this.createAt = LocalDateTime.parse(customLocalDateTimeFormat,
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }
}
