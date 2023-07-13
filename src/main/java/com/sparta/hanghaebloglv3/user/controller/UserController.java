package com.sparta.hanghaebloglv3.user.controller;

import com.sparta.hanghaebloglv3.common.dto.ApiResult;
import com.sparta.hanghaebloglv3.common.security.UserDetailsImpl;
import com.sparta.hanghaebloglv3.user.dto.PasswordRequestDto;
import com.sparta.hanghaebloglv3.user.dto.ProfileRequestDto;
import com.sparta.hanghaebloglv3.user.dto.ProfileResponseDto;
import com.sparta.hanghaebloglv3.user.dto.SignupRequestDto;
import com.sparta.hanghaebloglv3.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class
UserController {

  private final UserService userService;

  /**
   * Signup.
   */
  @PostMapping("/user/signup")
  public ApiResult signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
    return userService.signup(signupRequestDto);
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