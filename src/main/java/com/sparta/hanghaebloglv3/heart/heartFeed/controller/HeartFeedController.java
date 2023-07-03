package com.sparta.hanghaebloglv3.heart.heartFeed.controller;

import com.sparta.hanghaebloglv3.common.dto.ApiResult;
import com.sparta.hanghaebloglv3.heart.heartFeed.service.HeartFeedService;
import com.sparta.hanghaebloglv3.post.dto.PostResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HeartFeedController {
	private final HeartFeedService heartFeedService;

	@PostMapping("/heart-feed/{postId}")
	public PostResponseDto onClickFeedHeart(@PathVariable Long postId, HttpServletRequest request) {
		return heartFeedService.onClickFeedkHeart(postId, request);
	}

	@DeleteMapping("/heart-feed/{heartFeedId}")
	public ApiResult deleteFeedHeart(@PathVariable Long heartFeedId, HttpServletRequest request) {
		return heartFeedService.deleteFeedHeart(heartFeedId, request);
	}
}
