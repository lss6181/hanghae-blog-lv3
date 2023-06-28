package com.sparta.hanghaebloglv3.user.service;

import com.sparta.hanghaebloglv3.common.code.HanghaeBlogErrorCode;
import com.sparta.hanghaebloglv3.common.exception.HanghaeBlogException;
import com.sparta.hanghaebloglv3.common.jwt.JwtUtil;
import com.sparta.hanghaebloglv3.user.dto.LoginRequestDto;
import com.sparta.hanghaebloglv3.user.dto.SignupRequestDto;
import com.sparta.hanghaebloglv3.user.entity.UserEntity;
import com.sparta.hanghaebloglv3.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * UserService.
 */
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;

  /**
   * Signup.
   */
  @Transactional
  public void signup(SignupRequestDto signupRequestDto) {
    String username = signupRequestDto.getUsername();
    String password = signupRequestDto.getPassword();
    String role = signupRequestDto.getRole();

    // 회원 중복 확인
    Optional<UserEntity> found = userRepository.findByUsername(username);
    if (found.isPresent()) {
      throw new HanghaeBlogException(HanghaeBlogErrorCode.IN_USED_USERNAME, null);
    }

    UserEntity userEntity = new UserEntity(username, password, role);
    userRepository.save(userEntity);
  }

  /**
   * Login.
   */
  @Transactional(readOnly = true)
  public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
    String username = loginRequestDto.getUsername();
    String password = loginRequestDto.getPassword();

    // 사용자 확인
    UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(
        () -> new HanghaeBlogException(HanghaeBlogErrorCode.NOT_FOUND_USER, null)
    );

    // 비밀번호 확인
    if (!userEntity.getPassword().equals(password)) {
      throw new HanghaeBlogException(HanghaeBlogErrorCode.WRONG_PASSWORD, null);
    }
    // JWT Token 생성 및 반환
    response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(userEntity.getUsername()));
  }
}