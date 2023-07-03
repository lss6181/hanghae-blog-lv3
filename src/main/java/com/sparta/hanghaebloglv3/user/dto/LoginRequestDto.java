package com.sparta.hanghaebloglv3.user.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * LoginRequestDto.
 */
@Getter
@Setter
public class LoginRequestDto {
  // 로그인에서 필요한 정보를 담는 DTO
  private String id; // id 로그인으로 변경
  private String password;
}