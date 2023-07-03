package com.sparta.hanghaebloglv3.heart.heartComment.repository;

import com.sparta.hanghaebloglv3.heart.heartComment.entity.HeartComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeartCommentRepository extends JpaRepository<HeartComment, Long> {
}
