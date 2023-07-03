package com.sparta.hanghaebloglv3.heart.heartFeed.controller;

import com.sparta.hanghaebloglv3.heart.heartFeed.service.HeartFeedService;
import com.sparta.hanghaebloglv3.post.dto.PostResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HeartFeedController {
	private final HeartFeedService heartFeedService;
	@PostMapping("/heart-feed/{postId}")
	public PostResponseDto onClickHeart(@PathVariable Long postId, HttpServletRequest request){
		return heartFeedService.onClickHeart(postId, request);
	}
}
