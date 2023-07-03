package com.sparta.hanghaebloglv3.user.repository;

import com.sparta.hanghaebloglv3.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserRepository.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> { // id 타입 String으로 변경
  //  Optional<UserEntity> findByUsername(String username); // findById 사용
}