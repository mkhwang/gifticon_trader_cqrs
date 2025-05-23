package com.mkhwang.gifticon.service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "gifticon_likes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GifticonLike {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "gifticon_id", nullable = false, foreignKey = @ForeignKey(name = "fk_gifticon_like_gifticon"))
  private Gifticon gifticon;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_gifticon_like_user"))
  private User user;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
  }
}
