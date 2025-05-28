package com.mkhwang.trader.query.review.infra;

import com.mkhwang.trader.common.review.domain.QReview;
import com.mkhwang.trader.query.review.presentation.dto.ReviewDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ReviewQueryRepositoryImpl implements ReviewQueryRepository {
  private final JPAQueryFactory jpaQueryFactory;
  private final QReview qReview = QReview.review;

  @Override
  public ReviewDto.ReviewSummary getUserReviewSummary(Long id) {
    ReviewDto.FlatRatingStatDto flatRating = jpaQueryFactory
            .select(
                    Projections.constructor(ReviewDto.FlatRatingStatDto.class,
                            qReview.rating.avg(),
                            qReview.gifticon.count().intValue().as("count"),
                            Expressions.numberTemplate(Integer.class, "SUM(CASE WHEN {0} = 1 THEN 1 ELSE 0 END)", qReview.rating),
                            Expressions.numberTemplate(Integer.class, "SUM(CASE WHEN {0} = 2 THEN 1 ELSE 0 END)", qReview.rating),
                            Expressions.numberTemplate(Integer.class, "SUM(CASE WHEN {0} = 3 THEN 1 ELSE 0 END)", qReview.rating),
                            Expressions.numberTemplate(Integer.class, "SUM(CASE WHEN {0} = 4 THEN 1 ELSE 0 END)", qReview.rating),
                            Expressions.numberTemplate(Integer.class, "SUM(CASE WHEN {0} = 5 THEN 1 ELSE 0 END)", qReview.rating)
                    )

            )
            .from(qReview)
            .where(qReview.user.id.eq(id))
            .groupBy(qReview.user)
            .fetchOne();
    if (flatRating == null) {
      return ReviewDto.ReviewSummary.builder().averageRating(0d).totalCount(0).distribution(Map.of()).build();
    }
    return ReviewDto.ReviewSummary.builder()
            .averageRating(flatRating.getAverage())
            .totalCount(flatRating.getCount())
            .distribution(Map.of(
                    1, flatRating.getRating1(),
                    2, flatRating.getRating2(),
                    3, flatRating.getRating3(),
                    4, flatRating.getRating4(),
                    5, flatRating.getRating5()
            )).build();
  }
}
