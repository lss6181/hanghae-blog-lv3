package com.sparta.hanghaebloglv3.user.service;

import com.sparta.hanghaebloglv3.common.code.HanghaeBlogErrorCode;
import com.sparta.hanghaebloglv3.common.dto.ApiResult;
import com.sparta.hanghaebloglv3.common.exception.HanghaeBlogException;
import com.sparta.hanghaebloglv3.common.jwt.JwtUtil;
import com.sparta.hanghaebloglv3.user.dto.*;
import com.sparta.hanghaebloglv3.user.entity.UserEntity;
import com.sparta.hanghaebloglv3.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    String id = signupRequestDto.getId(); // id 추가
    String username = signupRequestDto.getUsername();
    String password = signupRequestDto.getPassword();
    String role = signupRequestDto.getRole();

    // 회원 중복 확인
    Optional<UserEntity> found = userRepository.findById(id); // findByUsername메서드 -> findById로 변경
    if (found.isPresent()) {
      throw new HanghaeBlogException(HanghaeBlogErrorCode.IN_USED_USERNAME, null);
    }

    UserEntity userEntity = new UserEntity(id, username, password, role); // id 추가
    userRepository.save(userEntity);
  }

  /**
   * Login.
   */
  @Transactional(readOnly = true)
  public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
    String id = loginRequestDto.getId();
    String password = loginRequestDto.getPassword();

    // 사용자 확인
//    UserEntity userEntity = userRepository.findByUsername(id).orElseThrow(
    UserEntity userEntity = userRepository.findById(id).orElseThrow( // findByUsername메서드 -> findById로 변경
            () -> new HanghaeBlogException(HanghaeBlogErrorCode.NOT_FOUND_USER, null)
    );

    // 비밀번호 확인
    if (!userEntity.getPassword().equals(password)) {
      throw new HanghaeBlogException(HanghaeBlogErrorCode.WRONG_PASSWORD, null);
    }
    // JWT Token 생성 및 반환
//    response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(userEntity.getUsername()));
    response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(id)); // getUsername() -> id 변경
  }

  @Transactional(readOnly = true)
  public ProfileResponseDto getProfile(HttpServletRequest request) {
    UserEntity userEntity = jwtUtil.checkToken(request); // 로그인 된 유저에 맞는 정보 담기

    return new ProfileResponseDto(userEntity.getUsername(), userEntity.getIntroduction()); // 해당 유저 정보 반환 / 메세지반환 필요없다고 하심
  }

  @Transactional
  public ApiResult checkPassword(PasswordRequestDto requestDto, HttpServletRequest request) {
    UserEntity userEntity = jwtUtil.checkToken(request);

    // 비밀번호 확인
    if (!userEntity.getPassword().equals(requestDto.getPassword())) {
      throw new HanghaeBlogException(HanghaeBlogErrorCode.WRONG_PASSWORD, null);
    }
    return new ApiResult("프로필 수정으로 넘어가기", HttpStatus.OK.value()); // 수정 페이지로 넘어가기 전 비밀번호 확인
  }

  @Transactional
  public ApiResult updateProfile(ProfileRequestDto requestDto, HttpServletRequest request) {
    UserEntity userEntity = jwtUtil.checkToken(request); // 로그인 된 유저에 맞는 정보 담기

    userEntity.update(requestDto); // 유저 정보 수정

    return new ApiResult("정보 수정 완료", HttpStatus.OK.value());
  }
}