package com.sparta.hanghaebloglv3.heart.heartComment.entity;

import com.sparta.hanghaebloglv3.comment.entity.CommentEntity;
import com.sparta.hanghaebloglv3.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "tb_heartComment")
public class HeartComment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "heartComment_id")
	private Long heartCommentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "commentId", referencedColumnName = "commentId")
	CommentEntity commentEntity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "username", referencedColumnName = "username")
	UserEntity userEntity;
}
