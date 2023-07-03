package com.sparta.hanghaebloglv3.comment.service;

import com.sparta.hanghaebloglv3.comment.dto.CommentRequestDto;
import com.sparta.hanghaebloglv3.comment.dto.CommentResponseDto;
import com.sparta.hanghaebloglv3.comment.entity.CommentEntity;
import com.sparta.hanghaebloglv3.comment.repository.CommentRepository;
import com.sparta.hanghaebloglv3.common.code.HanghaeBlogErrorCode;
import com.sparta.hanghaebloglv3.common.constant.ProjConst;
import com.sparta.hanghaebloglv3.common.dto.ApiResult;
import com.sparta.hanghaebloglv3.common.exception.HanghaeBlogException;
import com.sparta.hanghaebloglv3.common.jwt.JwtUtil;
import com.sparta.hanghaebloglv3.post.entity.PostEntity;
import com.sparta.hanghaebloglv3.post.repository.PostRepository;
import com.sparta.hanghaebloglv3.user.entity.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CommentService.
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;

    /**
     * Create Comment.
     */
    @Transactional
    public CommentResponseDto createComment(CommentRequestDto commentRequestDto, HttpServletRequest request) {

        /*
         * 토큰 검증.
         */
        UserEntity userEntity = jwtUtil.checkToken(request);

        if (userEntity == null) {
            throw new HanghaeBlogException(HanghaeBlogErrorCode.NOT_FOUND_USER, null);
        }

        /*
         * 댓글을 작성할 게시글이 존재하는지 확인.
         */
        PostEntity postEntity = postRepository.findById(commentRequestDto.getPostId()).orElseThrow(
                () -> new HanghaeBlogException(HanghaeBlogErrorCode.NOT_FOUND_POST, null)
        );

        /*
         * 댓글 저장.
         */
        CommentEntity entity = new CommentEntity();
        entity.setContent(commentRequestDto.getContent());
        entity.setUserEntity(userEntity);
        entity.setPostEntity(postEntity);

        commentRepository.save(entity);

        return CommentResponseDto.builder()
                .postId(postEntity.getPostId())
                .commentId(entity.getCommentId())
                .content(entity.getContent())
                .loginId(userEntity.getId())
                .createdAt(entity.getCreatedAt())
                .modifiedAt(entity.getModifiedAt())
                .heartCount(entity.getHeartCommentList().size())
                .build();
    }

    /**
     * Update comment.
     */
    @Transactional
    public CommentResponseDto updateComment(CommentRequestDto commentRequestDto, Long commentId, HttpServletRequest request) {

        /*
         * 토큰 검증.
         */
        UserEntity userEntity = this.checkJwtToken(request);

        /*
         * 작성한 댓글이 존재하는지 확인.
         */
        CommentEntity commentEntity = this.checkValidComment(commentId);

        /*
         * 수정하려고 하는 댓글의 작성자가 본인인지, 관리자 계정으로 수정하려고 하는지 확인.
         */
        if (this.checkValidUser(userEntity, commentEntity)) {
            throw new HanghaeBlogException(HanghaeBlogErrorCode.UNAUTHORIZED_USER, null);
        }

        /*
         * 작성된 댓글의 게시글 postId 와 입력한 게시글 postId 일치여부 확인.
         */
        if (this.checkPostId(commentRequestDto, commentEntity)) {
            throw new HanghaeBlogException(HanghaeBlogErrorCode.WRONG_POSTID, null);
        }


        commentEntity.setContent(commentRequestDto.getContent());
        commentRepository.save(commentEntity);

        return CommentResponseDto.builder()
                .postId(commentEntity.getPostEntity().getPostId())
                .commentId(commentEntity.getCommentId())
                .loginId(commentEntity.getUserEntity().getId())
                .content(commentEntity.getContent())
                .modifiedAt(commentEntity.getModifiedAt())
                .build();
    }

    /**
     * Delete comment.
     */
    @Transactional
    public ApiResult deleteComment(Long commentId, HttpServletRequest request) {

        /*
         * 토큰 검증.
         */
        UserEntity userEntity = this.checkJwtToken(request);

        /*
         * 삭제하려고 하는 댓글이 존재하는지 확인.
         */
        CommentEntity commentEntity = this.checkValidComment(commentId);

        /*
         * 삭제하려고 하는 댓글의 작성자가 본인인지, 관리자 계정으로 수정하려고 하는지 확인.
         */
        if (this.checkValidUser(userEntity, commentEntity)) {
            throw new HanghaeBlogException(HanghaeBlogErrorCode.UNAUTHORIZED_USER, null);
        }

        commentRepository.delete(commentEntity);

        return ApiResult.builder()
                .msg(ProjConst.API_CALL_SUCCESS)
                .statusCode(HttpStatus.OK.value())
                .build();
    }

    /**
     * Check JWT Token section.
     */
    private UserEntity checkJwtToken(HttpServletRequest request) {
        UserEntity userEntity = jwtUtil.checkToken(request);

        if (userEntity == null) {
            throw new HanghaeBlogException(HanghaeBlogErrorCode.NOT_FOUND_USER, null);
        }

        return userEntity;
    }

    /**
     * Check valid comment.
     */
    private CommentEntity checkValidComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new HanghaeBlogException(HanghaeBlogErrorCode.NOT_FOUND_COMMENT, null)
        );
    }

    /**
     * Check valid user.
     */
    private boolean checkValidUser(UserEntity userEntity, CommentEntity commentEntity) {
        boolean result = !(userEntity.getId().equals(commentEntity.getUserEntity().getId()))
                && !(userEntity.getRole().equals(ProjConst.ADMIN_ROLE));  // 작성자와 로그인사용자가 같지 않으면서 관리자계정도 아닌것이 true.
        return result;
    }

    /*
     * postId 일치여부 확인
     * true가 나오면 예외발생
     */
    private boolean checkPostId(CommentRequestDto commentRequestDto, CommentEntity commentEntity) {
        return commentRequestDto.getPostId() != commentEntity.getPostEntity().getPostId();
    }
}
