package com.zerobase.fintech.user.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

  @NotBlank(message = "아이디를 입력하세요.")
  private String userId;

  @NotBlank(message = "비밀번호를 입력하세요.")
  private String password;

  @NotBlank(message = "비밀번호를 입력하세요.")
  private String name;

  @NotBlank(message = "이메일을 입력하세요.")
  @Email(message = "이메일 형식에 맞게 입력하세요.")
  private String email;

  @NotBlank(message = "생년월일을 입력하세요.")
  @Size(min = 8, max = 8, message = "생년월일은 'YYYYMMDD' 형식으로 입력해야 합니다.")
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
