package com.sparta.hanghaebloglv3.user.controller;

import com.sparta.hanghaebloglv3.common.dto.ApiResult;
import com.sparta.hanghaebloglv3.user.dto.*;
import com.sparta.hanghaebloglv3.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * UserController.
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class UserController {

  private final UserService userService;

  /**
   * Signup.
   */
  @PostMapping("/signup")
  public ApiResult signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {

    userService.signup(signupRequestDto);

    return new ApiResult("회원가입 성공", HttpStatus.OK.value()); // 회원가입 성공시 ApiResult Dto를 사용하여 성공메세지와 statusCode를 띄움
  }

  /**
   * Login.
   */
  @PostMapping("/login")
  public ApiResult login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {

    userService.login(loginRequestDto, response);

    return new ApiResult("로그인 성공", HttpStatus.OK.value()); // 로그인 성공시 ApiResult Dto를 사용하여 성공메세지와 statusCode를 띄움
  }

  @GetMapping("/profile")
  public ProfileResponseDto getProfile(HttpServletRequest request) {
    return userService.getProfile(request);
  }

  @PostMapping("/profile")
  public ApiResult checkPassword(@RequestBody PasswordRequestDto requestDto, HttpServletRequest request) {
    return userService.checkPassword(requestDto, request);
  }

  @PutMapping("/profile")
  public ApiResult updateProfile(@RequestBody ProfileRequestDto requestDto, HttpServletRequest request) {
    return userService.updateProfile(requestDto, request);
  }
}