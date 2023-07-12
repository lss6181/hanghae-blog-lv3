package com.sparta.hanghaebloglv3.heart.heartComment.entity;

import com.sparta.hanghaebloglv3.comment.entity.CommentEntity;
import com.sparta.hanghaebloglv3.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "tb_heartComment")
public class HeartComment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "heartComment_id")
	private Long heartCommentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "comment_Id", referencedColumnName = "comment_Id")
	CommentEntity commentEntity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	UserEntity userEntity;

	public HeartComment(CommentEntity commentEntity, UserEntity userEntity) {
		this.commentEntity = commentEntity;
		this.userEntity = userEntity;
	}
}
