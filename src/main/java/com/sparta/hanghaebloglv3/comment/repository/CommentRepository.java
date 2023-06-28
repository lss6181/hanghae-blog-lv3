package com.sparta.hanghaebloglv3.comment.repository;

import com.sparta.hanghaebloglv3.comment.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * CommentRepository.
 */
@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByOrderByModifiedAtDesc();
}
