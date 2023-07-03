package com.sparta.hanghaebloglv3.heart.entity;

import com.sparta.hanghaebloglv3.comment.entity.CommentEntity;
import com.sparta.hanghaebloglv3.post.entity.PostEntity;
import com.sparta.hanghaebloglv3.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "tb_heartComment")
public class heartComment {
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
