package com.sparta.hanghaebloglv3.post.service;

import com.sparta.hanghaebloglv3.comment.dto.CommentResponseDto;
import com.sparta.hanghaebloglv3.comment.entity.CommentEntity;
import com.sparta.hanghaebloglv3.comment.repository.CommentRepository;
import com.sparta.hanghaebloglv3.common.code.HanghaeBlogErrorCode;
import com.sparta.hanghaebloglv3.common.exception.HanghaeBlogException;
import com.sparta.hanghaebloglv3.common.jwt.JwtUtil;
import com.sparta.hanghaebloglv3.post.dto.PostRequestDto;
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

/**
 * PostService.
 */
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;

    /**
     * Create Post.
     */
    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto, HttpServletRequest request) {

        // 토큰 체크 추가
        UserEntity userEntity = jwtUtil.checkToken(request);

        if (userEntity == null) {
            throw new HanghaeBlogException(HanghaeBlogErrorCode.NOT_FOUND_USER, null);
        }

        PostEntity postEntity = PostEntity.builder()
                .requestDto(requestDto)
                .userEntity(userEntity)
                .build();

        postRepository.save(postEntity);
        return new PostResponseDto(postEntity);
    }

    /**
     * Get all post.
     */
    @Transactional(readOnly = true) // readOnly true인 경우, JPA 영속성 컨텍스트에 갱신되지 않기 때문에, 조회 시 false로 설정하는 것보다 더 빠르게 조회가 가능함.
    public List<PostResponseDto> getPostList() {

        // Post Db > entityList
        List<PostEntity> postEntities = postRepository.findAllByOrderByModifiedAtDesc();
        // entityList > List<PostResponseDto>
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        postEntities.forEach(postEntity -> postResponseDtoList.add(new PostResponseDto(postEntity)));

        // Comment DB > entityList
        List<CommentEntity> commentEntityList = commentRepository.findAllByOrderByModifiedAtDesc();
        // entityList > List<CommentResponseDto>
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for (CommentEntity commentEntity : commentEntityList) {
            commentResponseDtoList.add(new CommentResponseDto(commentEntity.getCommentId(), commentEntity.getPostEntity().getPostId(), commentEntity.getContent(), commentEntity.getUserEntity().getUsername(), commentEntity.getCreatedAt(), commentEntity.getModifiedAt()));
        }

        // postId로 각 게시글에 달린 댓글 찾아 postResponseDto에 add 해주기.
        for (PostResponseDto postResponseDto : postResponseDtoList) {
            for (CommentResponseDto commentResponseDto : commentResponseDtoList) {
                if (postResponseDto.getContentId()==commentResponseDto.getPostId()){
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
    public PostResponseDto getPost(Long id) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new HanghaeBlogException(HanghaeBlogErrorCode.NOT_FOUND_POST, null));
        return new PostResponseDto(postEntity);
    }

    /**
     * Update post by id.
     */
    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, HttpServletRequest request) {

        // 토큰 체크 추가
        UserEntity userEntity = jwtUtil.checkToken(request);

        if (userEntity == null) {
            throw new HanghaeBlogException(HanghaeBlogErrorCode.NOT_FOUND_USER, null);
        }

        PostEntity postEntity = postRepository.findById(id).orElseThrow(
                () -> new HanghaeBlogException(HanghaeBlogErrorCode.NOT_FOUND_POST, null)
        );

        if (!postEntity.getUserEntity().equals(userEntity)) {
            throw new HanghaeBlogException(HanghaeBlogErrorCode.UNAUTHORIZED_USER, null);
        }

        postEntity.update(requestDto);
        return new PostResponseDto(postEntity);
    }

    /**
     * Delete post.
     */
    @Transactional
    public void deletePost(Long id, HttpServletRequest request) {

        // 토큰 체크 추가
        UserEntity userEntity = jwtUtil.checkToken(request);

        if (userEntity == null) {
            throw new HanghaeBlogException(HanghaeBlogErrorCode.NOT_FOUND_USER, null);
        }

        PostEntity postEntity = postRepository.findById(id).orElseThrow(
                () -> new HanghaeBlogException(HanghaeBlogErrorCode.NOT_FOUND_POST, null)
        );

        if (postEntity.getUserEntity().equals(userEntity)) {
            postRepository.delete(postEntity);
        }
    }
}