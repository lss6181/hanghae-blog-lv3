package com.sparta.hanghaebloglv3.heart.entity;

import com.sparta.hanghaebloglv3.post.entity.PostEntity;
import com.sparta.hanghaebloglv3.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "tb_heartFeed")
public class heartFeed {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "heartFeed_id")
	private Long heartFeedId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", referencedColumnName = "post_id")
	PostEntity postEntity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "username", referencedColumnName = "username")
	UserEntity userEntity;


}
