package com.sparta.hanghaebloglv3.comment.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * CommentResponseDto.
 */
@Getter
public class CommentResponseDto {

	private long commentId;
	private long postId;
	private String content;
	private String userName;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	private int heartCount;

	/**
	 * Initializer using Builder.
	 */
	@Builder
	public CommentResponseDto(long commentId, long postId, String content, String userName, LocalDateTime createdAt, LocalDateTime modifiedAt, int heartCount) {
		this.commentId = commentId;
		this.postId = postId;
		this.content = content;
		this.userName = userName;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		this.heartCount = heartCount;
	}
}
