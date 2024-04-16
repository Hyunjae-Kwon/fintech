package com.zerobase.fintech.user.service;

import com.zerobase.fintech.exception.CustomException;
import com.zerobase.fintech.exception.ErrorCode;
import com.zerobase.fintech.user.dao.UserRepository;
import com.zerobase.fintech.user.entity.SignInForm;
import com.zerobase.fintech.user.entity.SignUpForm;
import com.zerobase.fintech.user.entity.UserDto;
import com.zerobase.fintech.user.entity.UserEntity;
import com.zerobase.fintech.util.PasswordUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;

  public UserDto signUp(SignUpForm.Request request) {
    // 아이디 중복 확인
    if (userRepository.existsByUserId(request.getUserId())) {
      throw new CustomException(ErrorCode.DUPLICATED_USERID);
    }

    //기가입 회원 정보 확인
    if (userRepository.existsByNameAndBirth(request.getName(),
        request.getBirth())) {
      throw new CustomException(ErrorCode.ALREADY_SIGNUP_USER);
    }

    request.setPassword(PasswordUtils.encPassword(request.getPassword()));

    UserEntity save = userRepository.save(SignUpForm.Request.toEntity(request));

    log.info("User signup complete : {}", save);

    return UserDto.from(save);
  }

  public UserEntity authenticateUser(SignInForm form) {
    UserEntity user = userRepository.findByUserId(form.getUserId())
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    if (!PasswordUtils.equals(form.getPassword(), user.getPassword())) {
      throw new CustomException(ErrorCode.PASSWORD_INCORRECT);
    }

    return user;
  }

  @Override
  public UserDetails loadUserByUsername(String userId) {
    log.info("Load User => USERID : {}", userId);
    if (userRepository.existsByUserId(userId)) {
      return userRepository.findByUserId(userId)
          .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
    log.error("UserService -> loadUserByUsername FAILED");
    return null;
  }
}
