package com.sparta.hanghaebloglv3.heart.heartComment.service;

import com.sparta.hanghaebloglv3.comment.dto.CommentResponseDto;
import com.sparta.hanghaebloglv3.comment.entity.CommentEntity;
import com.sparta.hanghaebloglv3.comment.repository.CommentRepository;
import com.sparta.hanghaebloglv3.common.constant.ProjConst;
import com.sparta.hanghaebloglv3.common.dto.ApiResult;
import com.sparta.hanghaebloglv3.common.exception.IdNotFoundException;
import com.sparta.hanghaebloglv3.heart.heartComment.entity.HeartComment;
import com.sparta.hanghaebloglv3.heart.heartComment.repository.HeartCommentRepository;
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
public class HeartCommentService {

	private final HeartCommentRepository heartCommentRepository;
	private final CommentRepository commentRepository;
	private final MessageSource messageSource;

	@Transactional
	public CommentResponseDto onClickCommentHeart(Long commentId, UserEntity user) {

		// 좋아요 누른 댓글 find
		CommentEntity commentEntity = commentRepository.findById(commentId).orElseThrow(() ->
				new IdNotFoundException(
						messageSource.getMessage(
								"not.found.comment",
								null,
								"Not Found Comment",
								Locale.getDefault()
						)
				)
		);

		// 좋아요 누른 댓글이 본인 댓글이면 좋아요 불가능
		if (user.getUserId().equals(commentEntity.getUserEntity().getUserId())) {
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
		List<CommentEntity> commentEntityList = commentRepository.findAll();
		for (CommentEntity commentEntitys : commentEntityList) {
			if (commentEntitys.getCommentId().equals(commentEntity.getCommentId())
					&& commentEntitys.getUserEntity().getUserId().equals(user.getUserId())) {
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

		// HeartCommentRepository DB저장
		heartCommentRepository.save(new HeartComment(commentEntity, user));


		return CommentResponseDto.builder()
				.postId(commentEntity.getPostEntity().getPostId())
				.commentId(commentEntity.getCommentId())
				.content(commentEntity.getContent())
				.userName(commentEntity.getUserEntity().getUsername())
				.createdAt(commentEntity.getCreatedAt())
				.modifiedAt(commentEntity.getModifiedAt())
				.heartCount(commentEntity.getHeartCommentList().size())
				.build();
	}

	@Transactional
	public ApiResult deleteCommentHeart(Long heartCommentId, UserEntity user) {

		// HeartComment entity find
		HeartComment heartComment = heartCommentRepository.findById(heartCommentId).orElseThrow(() ->
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
		if (this.checkValidUser(user, heartComment)) {
			throw new IllegalArgumentException(
					messageSource.getMessage(
							"unauthorized.user",
							null,
							"Un Authorized User",
							Locale.getDefault()
					)
			);
		}

		heartCommentRepository.delete(heartComment);
		return new ApiResult(ProjConst.API_CALL_SUCCESS, HttpStatus.OK.value());
	}

	/**
	 * Check valid user.
	 */
	private boolean checkValidUser(UserEntity userEntity, HeartComment heartComment) {
		boolean result = !(userEntity.getUserId().equals(heartComment.getUserEntity().getUserId()))
				&& !(userEntity.getRole().equals(UserRoleEnum.ADMIN));  // 작성자와 로그인사용자가 같지 않으면서 관리자계정도 아닌것이 true.
		return result;
	}
}
