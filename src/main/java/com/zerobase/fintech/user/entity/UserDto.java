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
public class UserDto {

  private String userId;
  private String password;

  private String name;
  private String email;
  private String birth;

  private LocalDateTime createAt;

  public static UserDto from(UserEntity userEntity) {
    return UserDto.builder()
        .userId(userEntity.getUserId())
        .password(userEntity.getPassword())
        .name(userEntity.getName())
        .email(userEntity.getEmail())
        .birth(userEntity.getBirth())
        .createAt(userEntity.getCreateAt())
        .build();
  }
}
