package com.sparta.hanghaebloglv3.user.entity;

import com.sparta.hanghaebloglv3.user.dto.ProfileRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
  @Column(name = "login_id", nullable = false, unique = true)
  private String id;

  @Column(name = "username")
  private String username;

  @Column(name = "password", nullable = false)
  private String password;

  @Column
  private String introduction;

  @Column(name = "role", nullable = false)
  private String role;

  /**
   * Initializer.
   */
  public UserEntity(String id, String username, String password, String role) {
    this.id = id;
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
