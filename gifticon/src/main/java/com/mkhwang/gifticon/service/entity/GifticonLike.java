package com.mkhwang.gifticon.service.entity;

import com.mkhwang.gifticon.config.audit.BaseCreateAudit;
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
public class GifticonLike extends BaseCreateAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "gifticon_id", nullable = false, foreignKey = @ForeignKey(name = "fk_gifticon_like_gifticon"))
  private Gifticon gifticon;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_gifticon_like_user"))
  private User user;

}
