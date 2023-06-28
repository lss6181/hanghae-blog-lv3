package com.sparta.hanghaebloglv3.post.dto;

import lombok.Getter;

/**
 * PostRequestDto.
 */
@Getter
public class PostRequestDto {
  private String title;
  private String content;
  // 추가 요구사항 : post CRUD 연산 수행 시, 관리자 혹은 등록한 유저만 해당 게시글에 대한 수정 및 삭제를 할 수 있다.
  // todo: 게시글에 대한 추가 요구사항은 수강생이 직접 시도해볼 것.
}