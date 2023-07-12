package com.sparta.hanghaebloglv3.heart.heartFeed.controller;

import com.sparta.hanghaebloglv3.common.dto.ApiResult;
import com.sparta.hanghaebloglv3.common.security.UserDetailsImpl;
import com.sparta.hanghaebloglv3.heart.heartFeed.service.HeartFeedService;
import com.sparta.hanghaebloglv3.post.dto.PostResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HeartFeedController {
	private final HeartFeedService heartFeedService;

	@PostMapping("/heart-feed/{postId}")
	public PostResponseDto onClickFeedHeart(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		return heartFeedService.onClickFeedkHeart(postId, userDetails.getUser());
	}

	@DeleteMapping("/heart-feed/{heartFeedId}")
	public ApiResult deleteFeedHeart(@PathVariable Long heartFeedId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		return heartFeedService.deleteFeedHeart(heartFeedId, userDetails.getUser());
	}
}
