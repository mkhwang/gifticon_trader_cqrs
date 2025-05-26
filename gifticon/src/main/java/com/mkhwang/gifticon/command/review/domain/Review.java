package com.mkhwang.gifticon.command.review.domain;

import com.mkhwang.gifticon.command.gifticon.domain.Gifticon;
import com.mkhwang.gifticon.common.config.audit.BaseCreateUpdateAudit;
import com.mkhwang.gifticon.query.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review extends BaseCreateUpdateAudit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "gifticon_id", nullable = false, foreignKey = @ForeignKey(name = "fk_gifticon_review_gifticon"))
  private Gifticon gifticon;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_review_user_id"))
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reviewer", nullable = false, foreignKey = @ForeignKey(name = "fk_user_review"))
  private User reviewer;

  @Column(nullable = false)
  private Integer rating;

  @Column(length = 200)
  private String title;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String content;

  public static Review of(Gifticon gifticon, User reviewer, Integer rating, String title, String content) {
    Review review = new Review();
    review.gifticon = gifticon;
    review.user = gifticon.getSeller();
    review.reviewer = reviewer;
    review.rating = rating;
    review.title = title;
    review.content = content;

    if (!gifticon.isReviewable()) {
      throw new IllegalArgumentException("Gifticon is not reviewable.");
    }

    if (gifticon.isOwnedBy(reviewer.getId())) {
      throw new IllegalArgumentException("Reviewer must own the gifticon to write a review.");
    }

    if (gifticon.getBuyer() != reviewer) {
      throw new IllegalArgumentException("Reviewer must be the buyer of the gifticon.");
    }
    return review;
  }
}