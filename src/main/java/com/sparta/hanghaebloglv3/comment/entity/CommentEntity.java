package com.sparta.hanghaebloglv3.comment.entity;

import com.sparta.hanghaebloglv3.common.entity.Timestamped;
import com.sparta.hanghaebloglv3.heart.heartComment.entity.HeartComment;
import com.sparta.hanghaebloglv3.post.entity.PostEntity;
import com.sparta.hanghaebloglv3.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tb_comment")
public class CommentEntity extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Long commentId;

	@Column(name = "content", nullable = false)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", referencedColumnName = "post_id")
	private PostEntity postEntity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "login_id", referencedColumnName = "login_id")
	private UserEntity userEntity;

	@OneToMany(mappedBy = "commentEntity", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private List<HeartComment> heartCommentList = new ArrayList<>();


}