package com.sparta.hanghaebloglv3.heart.heartFeed.repository;

import com.sparta.hanghaebloglv3.heart.heartFeed.entity.HeartFeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeartFeedRepository extends JpaRepository<HeartFeed, Long> {
}