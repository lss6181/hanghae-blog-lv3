package com.sparta.hanghaebloglv3.post.controller;

import com.sparta.hanghaebloglv3.common.dto.ApiResult;
import com.sparta.hanghaebloglv3.common.security.UserDetailsImpl;
import com.sparta.hanghaebloglv3.post.dto.PostRequestDto;
import com.sparta.hanghaebloglv3.post.dto.PostResponseDto;
import com.sparta.hanghaebloglv3.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PostController.
 */
@RestController
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;

  /**
   * Create post.
   */
  @PostMapping("/api/post")
  public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return postService.createPost(requestDto, userDetails.getUser());
  }

  /**
   * Get post list.
   */
  @GetMapping("/api/post")
  public List<PostResponseDto> getPostList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    return postService.getPostList(userDetails.getUser());
  }

  /**
   * Get certain post.
   */
  @GetMapping("/api/post/{id}")
  public PostResponseDto getPost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return postService.getPost(id, userDetails.getUser());
  }

  /**
   * Update post.
   */
  @PutMapping("/api/post/{id}")
  public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return postService.updatePost(id, requestDto, userDetails.getUser());
  }

  /**
   * Delete post.
   */
  @DeleteMapping("/api/post/{id}")
  public ApiResult deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    return postService.deletePost(id, userDetails.getUser());
  }
}
