package com.zerobase.fintech.user.controller;

import com.zerobase.fintech.user.entity.SignUpForm;
import com.zerobase.fintech.user.entity.UserDto;
import com.zerobase.fintech.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "1. USER")
public class UserController {

  private final UserService userService;

  @Operation(summary = "회원가입")
  @PostMapping("/user/signUp")
  public ResponseEntity<?> signUpUser(@RequestBody SignUpForm.Request request){
    UserDto signedUpUser = userService.signUp(request);

    return ResponseEntity.ok(SignUpForm.Response.fromDto(signedUpUser));
  }

}
