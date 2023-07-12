package com.sparta.hanghaebloglv3.heart.heartComment.service;

import com.sparta.hanghaebloglv3.comment.dto.CommentResponseDto;
import com.sparta.hanghaebloglv3.comment.entity.CommentEntity;
import com.sparta.hanghaebloglv3.comment.repository.CommentRepository;
import com.sparta.hanghaebloglv3.common.code.HanghaeBlogErrorCode;
import com.sparta.hanghaebloglv3.common.constant.ProjConst;
import com.sparta.hanghaebloglv3.common.dto.ApiResult;
import com.sparta.hanghaebloglv3.common.exception.HanghaeBlogException;
import com.sparta.hanghaebloglv3.common.jwt.JwtUtil;
import com.sparta.hanghaebloglv3.heart.heartComment.entity.HeartComment;
import com.sparta.hanghaebloglv3.heart.heartComment.repository.HeartCommentRepository;
import com.sparta.hanghaebloglv3.user.entity.UserEntity;
import com.sparta.hanghaebloglv3.user.entity.UserRoleEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HeartCommentService {

	private final HeartCommentRepository heartCommentRepository;
	private final CommentRepository commentRepository;
	private final JwtUtil jwtUtil;

	@Transactional
	public CommentResponseDto onClickCommentHeart(Long commentId, UserEntity user) {

		// 좋아요 누른 댓글 find
		CommentEntity commentEntity = commentRepository.findById(commentId).orElseThrow(
				() -> new HanghaeBlogException(HanghaeBlogErrorCode.NOT_FOUND_COMMENT, null)
		);

		// 좋아요 누른 댓글이 본인 댓글이면 좋아요 불가능
		if (user.getUserId().equals(commentEntity.getUserEntity().getUserId())) {
			throw new HanghaeBlogException(HanghaeBlogErrorCode.CAN_NOT_MINE, null);
		}

		// 중복 좋아요 방지
		List<CommentEntity> commentEntityList = commentRepository.findAll();
		for (CommentEntity commentEntitys : commentEntityList) {
			if (commentEntitys.getCommentId().equals(commentEntity.getCommentId())
					&& commentEntitys.getUserEntity().getUserId().equals(user.getUserId())) {
				throw new HanghaeBlogException(HanghaeBlogErrorCode.OVERLAP_HEART, null);
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

	public ApiResult deleteCommentHeart(Long heartCommentId, UserEntity user) {

		// HeartComment entity find
		HeartComment heartComment = heartCommentRepository.findById(heartCommentId).orElseThrow(
				() -> new HanghaeBlogException(HanghaeBlogErrorCode.NOT_FOUND_HEART, null)
		);

		// 좋아요 누른 본인이거나 admin일경우만 삭제가능하도록 체크
		if (this.checkValidUser(user, heartComment)) {
			throw new HanghaeBlogException(HanghaeBlogErrorCode.UNAUTHORIZED_USER, null);
		}

		heartCommentRepository.delete(heartComment);
		return new ApiResult(ProjConst.API_CALL_SUCCESS, HttpStatus.OK.value());
	}

	/**
	 * Check valid user.
	 */
	private boolean checkValidUser(UserEntity userEntity, HeartComment heartComment) {
		boolean result = !(userEntity.getUserId().equals(heartComment.getUserEntity().getUserId()))
				&& !(userEntity.getRole().equals(UserRoleEnum.Authority.ADMIN));  // 작성자와 로그인사용자가 같지 않으면서 관리자계정도 아닌것이 true.
		return result;
	}
}
