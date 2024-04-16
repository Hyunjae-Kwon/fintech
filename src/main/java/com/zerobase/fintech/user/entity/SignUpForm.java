package com.zerobase.fintech.user.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class SignUpForm {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Request {

    private String userId;
    private String password;

    private String name;
    private String email;
    private String birth;

    private LocalDateTime createAt;

    public static UserEntity toEntity(Request request) {
      return UserEntity.builder()
          .userId(request.getUserId())
          .password(request.getPassword())
          .name(request.getName())
          .email(request.getEmail())
          .birth(request.getBirth())
          .createAt(LocalDateTime.now())
          .build();
    }
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Response {

    private String userId;
    private String name;
    private String email;
    private String birth;

    private LocalDateTime create_at;

    public static Response fromDto(UserDto userDto) {
      return Response.builder()
          .userId(userDto.getUserId())
          .name(userDto.getName())
          .email(userDto.getEmail())
          .birth(userDto.getBirth())
          .create_at(userDto.getCreateAt())
          .build();
    }
  }

}
