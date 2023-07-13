package com.sparta.hanghaebloglv3.heart.heartComment.controller;

import com.sparta.hanghaebloglv3.comment.dto.CommentResponseDto;
import com.sparta.hanghaebloglv3.common.dto.ApiResult;
import com.sparta.hanghaebloglv3.common.security.UserDetailsImpl;
import com.sparta.hanghaebloglv3.heart.heartComment.service.HeartCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HeartCommentController {
	private final HeartCommentService heartCommentService;

	@PostMapping("/heart-comment/{commentId}")
	public CommentResponseDto onClickCommentHeart(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		return heartCommentService.onClickCommentHeart(commentId, userDetails.getUser());
	}

	@DeleteMapping("/heart-comment/{heartCommentId}")
	public ApiResult deleteCommentHeart(@PathVariable Long heartCommentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
		return heartCommentService.deleteCommentHeart(heartCommentId, userDetails.getUser());
	}
}
