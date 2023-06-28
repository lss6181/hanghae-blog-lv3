package com.sparta.hanghaebloglv3.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * ApiResult.
 */
@Getter
@NoArgsConstructor
@ToString
public class ApiResult {
  // API result 반환을 위한 DTO
  // 성공 MSG와 status code(상태 코드)를 반환
  private String msg;
  private int statusCode;

  /**
   * Initializer using Builder.
   */
  @Builder
  public ApiResult(String msg, int statusCode) {
    this.msg = msg;
    this.statusCode = statusCode;
  }
}
