package com.sparta.hanghaebloglv3.heart.heartFeed.service;

import com.sparta.hanghaebloglv3.common.constant.ProjConst;
import com.sparta.hanghaebloglv3.common.dto.ApiResult;
import com.sparta.hanghaebloglv3.common.exception.IdNotFoundException;
import com.sparta.hanghaebloglv3.heart.heartFeed.entity.HeartFeed;
import com.sparta.hanghaebloglv3.heart.heartFeed.repository.HeartFeedRepository;
import com.sparta.hanghaebloglv3.post.dto.PostResponseDto;
import com.sparta.hanghaebloglv3.post.entity.PostEntity;
import com.sparta.hanghaebloglv3.post.repository.PostRepository;
import com.sparta.hanghaebloglv3.user.entity.UserEntity;
import com.sparta.hanghaebloglv3.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class HeartFeedService {

	private final PostRepository postRepository;
	private final HeartFeedRepository heartFeedRepository;
	private final MessageSource messageSource;

	@Transactional
	public PostResponseDto onClickFeedkHeart(Long postId, UserEntity user) {

		// 좋아요 누른 게시글 find
		PostEntity postEntity = postRepository.findById(postId).orElseThrow(() ->
				new IdNotFoundException(
						messageSource.getMessage(
								"not.found.post",
								null,
								"Not Found Post",
								Locale.getDefault()
						)
				)
		);

		// 좋아요 누른 게시글이 본인 게시글이면 좋아요 불가능
		if (user.getUserId().equals(postEntity.getUserEntity().getUserId())) {
			throw new IllegalArgumentException(
					messageSource.getMessage(
							"can.not.mine",
							null,
							"Can Not Mine",
							Locale.getDefault()
					)
			);
		}

		// 중복 좋아요 방지
		List<HeartFeed> heartFeedList = heartFeedRepository.findAll();
		for (HeartFeed heartFeeds : heartFeedList) {
			if (postEntity.getPostId().equals(heartFeeds.getPostEntity().getPostId())
					&& user.getIntroduction().equals(heartFeeds.getUserEntity().getUsername())) {
				throw new IllegalArgumentException(
						messageSource.getMessage(
								"overlap.heart",
								null,
								"Overlap Heart",
								Locale.getDefault()
						)
				);
			}
		}

		// HeartFeedRepository DB저장
		heartFeedRepository.save(new HeartFeed(postEntity, user));

		return new PostResponseDto(postEntity);
	}

	@Transactional
	public ApiResult deleteFeedHeart(Long heartFeedId, UserEntity user) {

		// HeartFeed entity find
		HeartFeed heartFeed = heartFeedRepository.findById(heartFeedId).orElseThrow(() ->
				new IdNotFoundException(
						messageSource.getMessage(
								"not.found.heart",
								null,
								"Not Found Heart",
								Locale.getDefault()
						)
				)
		);

		// 좋아요 누른 본인이거나 admin일 경우만 삭제가능하도록 체크
		if (this.checkValidUser(user, heartFeed)) {
			throw new IllegalArgumentException(
					messageSource.getMessage(
							"unauthorized.user",
							null,
							"Un Authorized User",
							Locale.getDefault()
					)
			);
		}

		heartFeedRepository.delete(heartFeed);

		return new ApiResult(ProjConst.API_CALL_SUCCESS, HttpStatus.OK.value());
	}

	/**
	 * Check valid user.
	 */
	private boolean checkValidUser(UserEntity userEntity, HeartFeed heartFeed) {
		boolean result = !(userEntity.getUserId().equals(heartFeed.getUserEntity().getUserId()))
				&& !(userEntity.getRole().equals(UserRoleEnum.ADMIN));  // 작성자와 로그인사용자가 같지 않으면서 관리자계정도 아닌것이 true.
		return result;
	}
}
