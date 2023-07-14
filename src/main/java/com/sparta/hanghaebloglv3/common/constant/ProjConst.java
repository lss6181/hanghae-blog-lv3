package com.sparta.hanghaebloglv3.common.constant;

/**
 * ProjConst
 */
public final class ProjConst { // class를 final로 선언하면, 오버라이딩이 불가능함.

  private ProjConst() {
    // 객체 생성을 금지함.
  }

  public static final String SIGN_UP_SUCCESS = "회원가입 성공";
  public static final String PASSWORD_CHECK_OK = "비밀번호 확인 성공";
  public static final String UPDATE_PROFILE_SUCCESS = "프로필 수정 성공";
  public static final String DELETE_SUCCESS = "삭제 성공";
  public static final String INVALID_TOKEN = "토큰이 유효하지 않습니다.";
  public static final String NOT_FOUND_TOKEN = "토큰을 찾을 수 없습니다.";

}
