package com.mkhwang.trader.common.gifticon.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "gifticon_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GifticonImage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "gifticon_id", nullable = false, foreignKey = @ForeignKey(name = "fk_gifticon_image_gifticon"))
  private Gifticon gifticon;

  @Column(nullable = false)
  private String url;

  @Column(name = "alt_text")
  private String altText;

  @Column(name = "is_primary", nullable = false)
  private boolean isPrimary;

  @Column(name = "display_order", nullable = false)
  private Integer displayOrder;

}
