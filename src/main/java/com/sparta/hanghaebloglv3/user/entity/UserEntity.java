package com.sparta.hanghaebloglv3.user.entity;

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
  @Column(name = "username")
  private String username;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "role", nullable = false)
  private String role;

  /**
   * Initializer.
   */
  public UserEntity(String username, String password, String role) {
    this.username = username;
    this.password = password;
    this.role = role;
  }
}

