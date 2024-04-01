package com.zerobase.fintech.user.service;

import static org.junit.jupiter.api.Assertions.*;

import com.zerobase.fintech.exception.CustomException;
import com.zerobase.fintech.exception.ErrorCode;
import com.zerobase.fintech.user.entity.SignInForm;
import com.zerobase.fintech.user.entity.SignUpForm;
import com.zerobase.fintech.user.entity.UserDto;
import com.zerobase.fintech.user.entity.UserEntity;
import com.zerobase.fintech.util.PasswordUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
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

  @BeforeEach
  void insertUser(){
    userService.signUp(SignUpForm.Request.builder()
        .userId("user1")
        .password("userpw")
        .name("테스트")
        .email("test@email.com")
        .birth("19900201")
        .build());
  }

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
        .userId("user1")
        .password("pw")
        .name("테스트1")
        .email("kwonhenrry93@gmail.com")
        .birth("19930210")
        .build();

    // when
    try {
      userService.signUp(request);

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
        .userId("user2")
        .password("testpw")
        .name("테스트")
        .email("kwonhenrry93@gmail.com")
        .birth("19900201")
        .build();

    // when
    try {
      userService.signUp(request);

      // then
    } catch (CustomException e) {
      assertEquals(e.getErrorCode(), ErrorCode.ALREADY_SIGNUP_USER);
      assertEquals(e.getErrorMessage(), ErrorCode.ALREADY_SIGNUP_USER.getDescription());
    }
  }

  @Test
  @DisplayName("User_SignIn_Success")
  void signInSuccess() {
    // given
    SignInForm signInForm = new SignInForm("user1", "userpw");

    // when
    UserEntity user = userService.authenticateUser(signInForm);

    // then
    assertEquals(user.getUserId(), "user1");
    assertTrue(PasswordUtils.equals("userpw", user.getPassword()));
    assertEquals(user.getName(), "테스트");
    assertEquals(user.getEmail(), "test@email.com");
    assertEquals(user.getBirth(), "19900201");
    log.info(String.valueOf(user.getCreateAt()));
  }


}