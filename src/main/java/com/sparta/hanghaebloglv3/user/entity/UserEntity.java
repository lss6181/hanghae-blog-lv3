package com.sparta.hanghaebloglv3.user.entity;

import com.sparta.hanghaebloglv3.user.dto.ProfileRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * UserEntity.
 */
@Getter
@NoArgsConstructor
@Entity
@Table(name = "tb_user")
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @Column(name = "username")
  private String username;

  @Column(name = "password", nullable = false)
  private String password;

  @Column
  private String introduction;

  @Column(name = "role", nullable = false)
  @Enumerated(value = EnumType.STRING)
  private UserRoleEnum role;

  /**
   * Initializer.
   */
  public UserEntity(String username, String password, UserRoleEnum role) {
    this.username = username;
    this.password = password;
    this.role = role;
  }

  public void update(ProfileRequestDto requestDto) {
    this.username = requestDto.getUsername();
    this.password = requestDto.getPassword();
    this.introduction = requestDto.getIntroduction();
  }
}

