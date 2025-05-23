package com.mkhwang.gifticon.service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "gifticons")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Gifticon {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String slug;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seller_id", nullable = false, foreignKey = @ForeignKey(name = "fk_gifticon_seller"))
  private User seller;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "buyer_id", foreignKey = @ForeignKey(name = "fk_gifticon_buyer"))
  private User buyer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "brand_id", nullable = false, foreignKey = @ForeignKey(name = "fk_gifticon_brand"))
  private Brand brand;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ProductStatus status;

  @OneToOne(mappedBy = "gifticon", cascade = CascadeType.ALL, orphanRemoval = true)
  private GifticonPrice price;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(name = "fk_gifticon_category"))
  private Category category;

  @OneToMany(mappedBy = "gifticon", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<GifticonImage> images = new ArrayList<>();

  // Tag도 ManyToMany 관계로 직접 참조
  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
          name = "gifticon_tags",
          joinColumns = @JoinColumn(name = "gifticon_id", foreignKey = @ForeignKey(name = "fk_gifticon_tag")),
          inverseJoinColumns = @JoinColumn(name = "tag_id", foreignKey = @ForeignKey(name = "fk_tag_gifticon"))
  )
  @Builder.Default
  private List<Tag> tags = new ArrayList<>();

  @OneToMany(mappedBy = "gifticon", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Review> reviews = new ArrayList<>();

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}
