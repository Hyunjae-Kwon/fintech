package com.zerobase.fintech.user.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpForm {

  private String userId;
  private String password;

  private String name;
  private String email;
  private String birth;

  private LocalDateTime createAt;

  public static UserEntity toEntity(SignUpForm request) {
    return UserEntity.builder()
        .userId(request.getUserId())
        .password(request.getPassword())
        .name(request.getName())
        .email(request.getEmail())
        .birth(request.getBirth())
        .createAt(LocalDateTime.now())
        .build();
  }

  public static SignUpForm fromDto(UserEntity userEntity) {
    return SignUpForm.builder()
        .userId(userEntity.getUserId())
        .name(userEntity.getName())
        .email(userEntity.getEmail())
        .birth(userEntity.getBirth())
        .createAt(userEntity.getCreateAt())
        .build();
  }
}
