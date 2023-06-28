package com.sparta.hanghaebloglv3.comment.controller;

import com.sparta.hanghaebloglv3.comment.dto.CommentRequestDto;
import com.sparta.hanghaebloglv3.comment.dto.CommentResponseDto;
import com.sparta.hanghaebloglv3.comment.service.CommentService;
import com.sparta.hanghaebloglv3.common.dto.ApiResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
  public CommentResponseDto createComment(@RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request) {
    return commentService.createComment(commentRequestDto, request);
  }

  /**
   * Update comment.
   */
  @PutMapping("/api/comment/{id}")
  public CommentResponseDto updateComment(@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest request) {
    return commentService.updateComment(commentRequestDto, id, request);
  }

  /**
   * Delete comment.
   */
  @DeleteMapping("/api/comment/{id}")
  public ApiResult deleteComment(@PathVariable Long id, HttpServletRequest request) {
    return commentService.deleteComment(id, request);
  }
}
