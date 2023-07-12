package com.sparta.hanghaebloglv3.user.controller;

import com.sparta.hanghaebloglv3.common.dto.ApiResult;
import com.sparta.hanghaebloglv3.common.security.UserDetailsImpl;
import com.sparta.hanghaebloglv3.user.dto.*;
import com.sparta.hanghaebloglv3.user.entity.UserEntity;
import com.sparta.hanghaebloglv3.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * UserController.
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

  private final UserService userService;

  /**
   * Signup.
   */
  @PostMapping("/user/signup")
  public ApiResult signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {

    userService.signup(signupRequestDto);

    return new ApiResult("회원가입 성공", HttpStatus.OK.value()); // 회원가입 성공시 ApiResult Dto를 사용하여 성공메세지와 statusCode를 띄움
  }

  // 로그인 성공
  @GetMapping("/user/login-success")
  public ApiResult successLogin() {

    return new ApiResult("로그인 성공", HttpStatus.OK.value()); // 로그인 성공시 ApiResult Dto를 사용하여 성공메세지와 statusCode를 띄움
  }

  // 프로필 조회
  @GetMapping("/profile")
  public ProfileResponseDto getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    return userService.getProfile(userDetails.getUser());
  }

  // 프로필 수정 전 패스워드 재확인
  @PostMapping("/profile/valid-password")
  public ApiResult checkPassword(@RequestBody PasswordRequestDto passwordRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return userService.checkPassword(passwordRequestDto, userDetails.getUser());
  }

  // 프로필 수정
  @PutMapping("/profile")
  public ApiResult updateProfile(@RequestBody ProfileRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return userService.updateProfile(requestDto, userDetails.getUser());
  }
}