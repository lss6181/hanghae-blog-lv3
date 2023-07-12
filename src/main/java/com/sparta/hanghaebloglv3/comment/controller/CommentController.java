package com.sparta.hanghaebloglv3.comment.controller;

import com.sparta.hanghaebloglv3.comment.dto.CommentRequestDto;
import com.sparta.hanghaebloglv3.comment.dto.CommentResponseDto;
import com.sparta.hanghaebloglv3.comment.service.CommentService;
import com.sparta.hanghaebloglv3.common.dto.ApiResult;
import com.sparta.hanghaebloglv3.common.security.UserDetailsImpl;
import com.sparta.hanghaebloglv3.user.entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * CommentController.
 */
@RestController
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  /**
   * Create comment.
   */
  @PostMapping("/api/comment")
  public CommentResponseDto createComment(@RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return commentService.createComment(commentRequestDto, userDetails.getUser());
  }

  /**
   * Update comment.
   */
  @PutMapping("/api/comment/{id}")
  public CommentResponseDto updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return commentService.updateComment(commentRequestDto, commentId, userDetails.getUser());
  }

  /**
   * Delete comment.
   */
  @DeleteMapping("/api/comment/{id}")
  public ApiResult deleteComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return commentService.deleteComment(id, userDetails.getUser());
  }
}
