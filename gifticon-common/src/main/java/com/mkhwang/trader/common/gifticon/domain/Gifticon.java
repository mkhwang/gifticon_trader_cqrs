package com.mkhwang.trader.common.gifticon.domain;

import com.mkhwang.trader.common.brand.domain.Brand;
import com.mkhwang.trader.common.category.domain.Category;
import com.mkhwang.trader.common.config.audit.BaseCreateUpdateAudit;
import com.mkhwang.trader.common.review.domain.Review;
import com.mkhwang.trader.common.tag.domain.Tag;
import com.mkhwang.trader.common.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(name = "gifticons")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Gifticon extends BaseCreateUpdateAudit {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String name;

  private String slug;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(nullable = false)
  private LocalDate dueDate;

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
  private GifticonStatus status;

  @OneToOne(mappedBy = "gifticon", cascade = CascadeType.ALL, orphanRemoval = true)
  private GifticonPrice price;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false, foreignKey = @ForeignKey(name = "fk_gifticon_category"))
  private Category category;

  @OneToMany(mappedBy = "gifticon", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<GifticonImage> images = new ArrayList<>();

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

  public boolean isOwnedBy(Long reviewerId) {
    return this.seller != null && this.seller.getId().equals(reviewerId);
  }

  public boolean isTradeAble() {
    return this.status == GifticonStatus.ON_SALE;
  }

  public boolean isReviewable() {
    return this.status == GifticonStatus.SOLD_OUT && this.buyer != null;
  }

  public boolean isDeleteAble() {
    return this.status == GifticonStatus.ON_SALE;
  }

  public void buy(User user) {
    if (this.isOwnedBy(user.getId())) {
      throw new IllegalArgumentException("Seller cannot buy their own gifticon");
    }
    if (!this.isTradeAble()) {
      throw new IllegalArgumentException("Gifticon is not available for trade");
    }

    this.status = GifticonStatus.SOLD_OUT;
    this.buyer = user;
  }

  public void delete(Long userId) {
    if (!this.isOwnedBy(userId)) {
      throw new IllegalArgumentException("User does not own this gifticon");
    }
    if (!this.isDeleteAble()) {
      throw new IllegalArgumentException("Gifticon cannot be deleted.");
    }
    this.status = GifticonStatus.DELETED;
  }
}
