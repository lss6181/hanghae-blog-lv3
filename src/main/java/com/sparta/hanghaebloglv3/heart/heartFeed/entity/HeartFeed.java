package com.sparta.hanghaebloglv3.heart.heartFeed.entity;

import com.sparta.hanghaebloglv3.post.entity.PostEntity;
import com.sparta.hanghaebloglv3.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "tb_heartFeed")
public class HeartFeed {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "heartFeed_id")
	private Long heartFeedId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", referencedColumnName = "post_id")
	PostEntity postEntity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	UserEntity userEntity;

	public HeartFeed(PostEntity postEntity, UserEntity userEntity) {
		this.postEntity = postEntity;
		this.userEntity = userEntity;
	}

}
