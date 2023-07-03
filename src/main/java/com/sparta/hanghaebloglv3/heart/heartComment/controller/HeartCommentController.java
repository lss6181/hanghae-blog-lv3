package com.sparta.hanghaebloglv3.heart.heartComment.controller;

import com.sparta.hanghaebloglv3.comment.dto.CommentResponseDto;
import com.sparta.hanghaebloglv3.heart.heartComment.service.HeartCommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HeartCommentController {
	private final HeartCommentService heartCommentService;

	@PostMapping("/heart-comment/{commentId}")
	public CommentResponseDto onClickCommentHeart(@PathVariable Long commentId, HttpServletRequest request) {
		return heartCommentService.onClickCommentHeart(commentId, request);
	}
}
