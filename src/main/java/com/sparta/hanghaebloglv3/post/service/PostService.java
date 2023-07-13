package com.sparta.hanghaebloglv3.post.service;

import com.sparta.hanghaebloglv3.comment.dto.CommentResponseDto;
import com.sparta.hanghaebloglv3.comment.entity.CommentEntity;
import com.sparta.hanghaebloglv3.comment.repository.CommentRepository;
import com.sparta.hanghaebloglv3.common.constant.ProjConst;
import com.sparta.hanghaebloglv3.common.dto.ApiResult;
import com.sparta.hanghaebloglv3.common.exception.IdNotFoundException;
import com.sparta.hanghaebloglv3.common.jwt.JwtUtil;
import com.sparta.hanghaebloglv3.post.dto.PostRequestDto;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * PostService.
 */
@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	private final MessageSource messageSource;
	private final JwtUtil jwtUtil;

	/**
	 * Create Post.
	 */
	@Transactional
	public PostResponseDto createPost(PostRequestDto requestDto, UserEntity user) {

		PostEntity postEntity = PostEntity.builder()
				.requestDto(requestDto)
				.userEntity(user)
				.build();

		postRepository.save(postEntity);
		return new PostResponseDto(postEntity);
	}

	/**
	 * Get all post.
	 */
	@Transactional(readOnly = true) // readOnly true인 경우, JPA 영속성 컨텍스트에 갱신되지 않기 때문에, 조회 시 false로 설정하는 것보다 더 빠르게 조회가 가능함.
	public List<PostResponseDto> getPostList(UserEntity user) {

		// Post Db > List<PostEntity>
		List<PostEntity> postEntities = postRepository.findAllByOrderByModifiedAtDesc();

		// List<PostEntity> > List<PostResponseDto>
		List<PostResponseDto> postResponseDtoList = new ArrayList<>();
		postEntities.forEach(postEntity -> postResponseDtoList.add(new PostResponseDto(postEntity)));

		// for문으로 게시글 하나 씩 돌 때 마다 댓글전체도 돌려 postId로 매칭시켜 postResponseDto에 댓글 add 해주기.
		for (PostResponseDto postResponseDto : postResponseDtoList) {
			for (CommentResponseDto commentResponseDto : this.getCommentResponseDtoList()) {
				if (postResponseDto.getPostId() == commentResponseDto.getPostId()) {
					postResponseDto.addCommentResponseDtoList(commentResponseDto);
				}
			}
		}
		return postResponseDtoList;
	}

	/**
	 * Get post by id.
	 */
	@Transactional(readOnly = true)
	public PostResponseDto getPost(Long id, UserEntity user) {

		PostEntity postEntity = postRepository.findById(id).orElseThrow(() ->
				new IdNotFoundException(
						messageSource.getMessage(
								"not.found.post",
								null,
								"Not Found Post",
								Locale.getDefault()
						)
				)
		);

		PostResponseDto postResponseDto = new PostResponseDto(postEntity);

		// for문으로 전체댓글 돌려 선택된 게시글과 postId로 매칭시켜 해당 게시글의 댓글까지 보이게 하기
		for (CommentResponseDto commentResponseDto : this.getCommentResponseDtoList()) {
			if (postResponseDto.getPostId() == commentResponseDto.getPostId()) {
				postResponseDto.addCommentResponseDtoList(commentResponseDto);
			}
		}

		return postResponseDto;
	}

	/**
	 * Update post by id.
	 */
	@Transactional
	public PostResponseDto updatePost(Long id, PostRequestDto requestDto, UserEntity user) {

		PostEntity postEntity = postRepository.findById(id).orElseThrow(() ->
				new IdNotFoundException(
						messageSource.getMessage(
								"not.found.post",
								null,
								"Not Found Post",
								Locale.getDefault()
						)
				)
		);

		/*
		 * 수정하려고 하는 댓글의 작성자가 본인인지, 관리자 계정으로 수정하려고 하는지 확인.
		 */
		if (this.checkValidUser(user, postEntity)) {
			throw new IllegalArgumentException(
					messageSource.getMessage(
							"unauthorized.user",
							null,
							"Un Authorized User",
							Locale.getDefault()
					)
			);
		}

		postEntity.update(requestDto);

		return new PostResponseDto(postEntity);
	}

	/**
	 * Delete post.
	 */
	@Transactional
	public ApiResult deletePost(Long id, UserEntity user) {

		PostEntity postEntity = postRepository.findById(id).orElseThrow(() ->
				new IdNotFoundException(
						messageSource.getMessage(
								"not.found.post",
								null,
								"Not Found Post",
								Locale.getDefault()
						)
				)
		);

		/*
		 * 수정하려고 하는 댓글의 작성자가 본인인지, 관리자 계정으로 수정하려고 하는지 확인.
		 */
		if (this.checkValidUser(user, postEntity)) {
			throw new IllegalArgumentException(
					messageSource.getMessage(
							"unauthorized.user",
							null,
							"Un Authorized User",
							Locale.getDefault()
					)
			);
		}

		postRepository.delete(postEntity);

		return new ApiResult(ProjConst.DELETE_SUCCESS, HttpStatus.OK.value());
	}

	// 전체 댓글 ResponseDto List로 만들기
	private List<CommentResponseDto> getCommentResponseDtoList() {
		// Comment DB > entityList
		List<CommentEntity> commentEntityList = commentRepository.findAllByOrderByModifiedAtDesc();

		// entityList > List<CommentResponseDto>
		List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
		for (CommentEntity commentEntity : commentEntityList) {

			CommentResponseDto commentResponseDto = CommentResponseDto.builder()
					.postId(commentEntity.getPostEntity().getPostId())
					.commentId(commentEntity.getCommentId())
					.content(commentEntity.getContent())
					.userName(commentEntity.getUserEntity().getUsername())
					.createdAt(commentEntity.getCreatedAt())
					.modifiedAt(commentEntity.getModifiedAt())
					.heartCount(commentEntity.getHeartCommentList().size())
					.build();
			commentResponseDtoList.add(commentResponseDto);
		}
		return commentResponseDtoList;
	}

	/**
	 * Check valid user.
	 */
	private boolean checkValidUser(UserEntity userEntity, PostEntity postEntity) {
		boolean result = !(userEntity.getUserId().equals(postEntity.getUserEntity().getUserId()))
				&& !(userEntity.getRole().equals(UserRoleEnum.ADMIN));  // 작성자와 로그인사용자가 같지 않으면서 관리자계정도 아닌것이 true.
		return result;
	}
}