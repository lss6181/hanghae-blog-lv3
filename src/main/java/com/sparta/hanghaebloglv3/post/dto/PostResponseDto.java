package com.sparta.hanghaebloglv3.post.dto;

import com.sparta.hanghaebloglv3.comment.dto.CommentResponseDto;
import com.sparta.hanghaebloglv3.post.entity.PostEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * PostResponseDto.
 */
@Getter
public class PostResponseDto { // 게시물 CRUD 요청에 대한 응답으로 사용되는 DTO
    private long contentId;
    private String title;
    private String contents;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> commentList = new ArrayList<>();

    /**
     * initializer using Builder.
     */
    @Builder
    public PostResponseDto(PostEntity postEntity) {
        this.contentId = postEntity.getPostId();
        this.title = postEntity.getTitle();
        this.contents = postEntity.getContent();
        this.username = postEntity.getUserEntity().getUsername();
        this.createdAt = postEntity.getCreatedAt();
        this.modifiedAt = postEntity.getModifiedAt();
    }

    public void addCommentResponseDtoList(CommentResponseDto responseDto) {
        this.commentList.add(responseDto);
    }
}