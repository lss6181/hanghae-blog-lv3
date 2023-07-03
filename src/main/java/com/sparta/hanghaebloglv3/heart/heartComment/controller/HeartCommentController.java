package com.sparta.hanghaebloglv3.heart.heartComment.controller;

import com.sparta.hanghaebloglv3.comment.dto.CommentResponseDto;
import com.sparta.hanghaebloglv3.common.dto.ApiResult;
import com.sparta.hanghaebloglv3.heart.heartComment.service.HeartCommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HeartCommentController {
	private final HeartCommentService heartCommentService;

	@PostMapping("/heart-comment/{commentId}")
	public CommentResponseDto onClickCommentHeart(@PathVariable Long commentId, HttpServletRequest request) {
		return heartCommentService.onClickCommentHeart(commentId, request);
	}

	@DeleteMapping("/heart-comment/{heartCommentId}")
	public ApiResult deleteCommentHeart(@PathVariable Long heartCommentId,HttpServletRequest request){
		return heartCommentService.deleteCommentHeart(heartCommentId, request);
	}
}
