package com.zerobase.fintech.user.service;

import static org.junit.jupiter.api.Assertions.*;

import com.zerobase.fintech.exception.CustomException;
import com.zerobase.fintech.exception.ErrorCode;
import com.zerobase.fintech.user.entity.SignUpForm;
import com.zerobase.fintech.user.entity.UserDto;
import com.zerobase.fintech.util.PasswordUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
@Transactional
class UserServiceTest {

  @Autowired
  UserService userService;

  @Test
  @DisplayName("User_SignUp_Success")
  void signUpSuccess() {
    // given
    SignUpForm.Request request = SignUpForm.Request.builder()
        .userId("test")
        .password("pw")
        .name("테스트")
        .email("kwonhenrry93@gmail.com")
        .birth("19930211")
        .build();

    // when
    UserDto userDto = userService.signUp(request);

    // then
    log.info("Create At : {}", userDto.getCreateAt());
    assertEquals(userDto.getUserId(), "test");
    assertTrue(PasswordUtils.equals("pw", userDto.getPassword()));
    assertEquals(userDto.getName(), "테스트");
    assertEquals(userDto.getEmail(), "kwonhenrry93@gmail.com");
    assertEquals(userDto.getBirth(), "19930211");
  }

  @Test
  @DisplayName("User_SignUp_Fail - Duplicated_UserId")
  void signUpDuplicatedUserIdFail() {
    // given
    SignUpForm.Request request = SignUpForm.Request.builder()
        .userId("test")
        .password("pw")
        .name("테스트")
        .email("kwonhenrry93@gmail.com")
        .birth("19930211")
        .build();

    SignUpForm.Request request2 = SignUpForm.Request.builder()
        .userId("test")
        .password("test")
        .name("테스트2")
        .email("kwonhenrry93@gmail.com")
        .birth("19930212")
        .build();

    // when
    userService.signUp(request);
    try {
      userService.signUp(request2);

    // then
    } catch (CustomException e) {
      assertEquals(e.getErrorCode(), ErrorCode.DUPLICATED_USERID);
      assertEquals(e.getErrorMessage(), ErrorCode.DUPLICATED_USERID.getDescription());
    }
  }

  @Test
  @DisplayName("User_SignUp_Fail - Duplicated_UserInfo")
  void signUpDuplicatedUserInfoFail() {
    // given
    SignUpForm.Request request = SignUpForm.Request.builder()
        .userId("test")
        .password("pw")
        .name("테스트")
        .email("kwonhenrry93@gmail.com")
        .birth("19930211")
        .build();

    SignUpForm.Request request2 = SignUpForm.Request.builder()
        .userId("test2")
        .password("test")
        .name("테스트")
        .email("kwonhenrry93@gmail.com")
        .birth("19930211")
        .build();

    // when
    userService.signUp(request);
    try {
      userService.signUp(request2);

      // then
    } catch (CustomException e) {
      assertEquals(e.getErrorCode(), ErrorCode.ALREADY_SIGNUP_USER);
      assertEquals(e.getErrorMessage(), ErrorCode.ALREADY_SIGNUP_USER.getDescription());
    }
  }
}