package com.sparta.hanghaebloglv3.heart.heartFeed.repository;

import com.sparta.hanghaebloglv3.heart.heartFeed.entity.HeartFeed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartFeedRepository extends JpaRepository<HeartFeed, Long> {
}