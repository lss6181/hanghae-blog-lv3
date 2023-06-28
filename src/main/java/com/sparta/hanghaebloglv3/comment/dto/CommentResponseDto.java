package com.sparta.hanghaebloglv3.comment.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * CommentResponseDto.
 */
@Getter
public class CommentResponseDto {

  private long commentId;
  private long postId;
  private String content;
  private String username;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;

  /**
   * Initializer using Builder.
   */
  @Builder
  public CommentResponseDto(long commentId, long postId, String content, String username, LocalDateTime createdAt, LocalDateTime modifiedAt) {
    this.commentId = commentId;
    this.postId = postId;
    this.content = content;
    this.username = username;
    this.createdAt = createdAt;
    this.modifiedAt = modifiedAt;
  }
}
