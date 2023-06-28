package com.sparta.hanghaebloglv3.post.controller;

import com.sparta.hanghaebloglv3.common.dto.ApiResult;
import com.sparta.hanghaebloglv3.post.dto.PostRequestDto;
import com.sparta.hanghaebloglv3.post.dto.PostResponseDto;
import com.sparta.hanghaebloglv3.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
  public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, HttpServletRequest request) {  // 객체 형식으로 넘어오기 때문에 RequestBody를 사용
    return postService.createPost(requestDto, request);
  }

  /**
   * Get post list.
   */
  @GetMapping("/api/post")
  public List<PostResponseDto> getPostList(HttpServletRequest request) {
    return postService.getPostList(request);
  }

  /**
   * Get certain post.
   */
  @GetMapping("/api/post/{id}")
  public PostResponseDto getPost(@PathVariable Long id) {
    return postService.getPost(id);
  }

  /**
   * Update post.
   */
  @PutMapping("/api/post/{id}")
  public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, HttpServletRequest request) {
    return postService.updatePost(id, requestDto, request);
  }

  /**
   * Delete post.
   */
  @DeleteMapping("/api/post/{id}")
  public ApiResult deletePost(@PathVariable Long id, HttpServletRequest request) {
    postService.deletePost(id, request);
    return new ApiResult("게시글 삭제 성공", HttpStatus.OK.value()); // 게시글 삭제 성공시 ApiResult Dto를 사용하여 성공메세지와 statusCode를 띄움
  }
}
