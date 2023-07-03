package com.sparta.hanghaebloglv3.heart.heartFeed.service;

import com.sparta.hanghaebloglv3.common.code.HanghaeBlogErrorCode;
import com.sparta.hanghaebloglv3.common.exception.HanghaeBlogException;
import com.sparta.hanghaebloglv3.common.jwt.JwtUtil;
import com.sparta.hanghaebloglv3.heart.heartFeed.entity.HeartFeed;
import com.sparta.hanghaebloglv3.heart.heartFeed.repository.HeartFeedRepository;
import com.sparta.hanghaebloglv3.post.dto.PostResponseDto;
import com.sparta.hanghaebloglv3.post.entity.PostEntity;
import com.sparta.hanghaebloglv3.post.repository.PostRepository;
import com.sparta.hanghaebloglv3.user.entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HeartFeedService {

	private final PostRepository postRepository;
	private final HeartFeedRepository heartFeedRepository;
	private final JwtUtil jwtUtil;

	@Transactional
	public PostResponseDto onClickHeart(Long postId, HttpServletRequest request) {
		// 토큰 체크
		UserEntity userEntity = jwtUtil.checkToken(request);

		if (userEntity == null) {
			throw new HanghaeBlogException(HanghaeBlogErrorCode.NOT_FOUND_USER, null);
		}

		// 좋아요 누른 게시글 find
		PostEntity postEntity = postRepository.findById(postId)
				.orElseThrow(() -> new HanghaeBlogException(HanghaeBlogErrorCode.NOT_FOUND_POST, null));

		// 좋아요누른게시글이 로그인사용자 본인게시글이면 좋아요 불가능
		if (userEntity.getId().equals(postEntity.getUserEntity().getId())) {
			throw new HanghaeBlogException(HanghaeBlogErrorCode.CAN_NOT_MINE,null);
		}

		// 중복 좋아요 방지
		List<HeartFeed> heartFeedList = heartFeedRepository.findAll();
		for (HeartFeed heartFeeds : heartFeedList) {
			if (postEntity.getPostId().equals(heartFeeds.getPostEntity().getPostId())
					&& userEntity.getId().equals(heartFeeds.getUserEntity().getId())) {
				throw new HanghaeBlogException(HanghaeBlogErrorCode.OVERLAP_HEART, null);
			}
		}

		// HeartFeedRepository DB저장
		heartFeedRepository.save(new HeartFeed(postEntity, userEntity));

		return new PostResponseDto(postEntity);
	}
}
