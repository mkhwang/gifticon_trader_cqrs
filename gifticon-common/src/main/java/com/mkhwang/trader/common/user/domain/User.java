package com.mkhwang.trader.common.user.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "users")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false, unique = true)
  private String username;
  @Column(nullable = false)
  private String nickname;
  private String profileImageUrl;

  public static User of(String username, String nickname, String profileImageUrl) {
    return new User(null, username, nickname, profileImageUrl);
  }
}
