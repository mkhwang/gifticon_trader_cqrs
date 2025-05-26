package com.mkhwang.gifticon.query.review.application;

import com.mkhwang.gifticon.command.review.domain.QReview;
import com.mkhwang.gifticon.common.config.QuerydslUtil;
import com.mkhwang.gifticon.common.dto.PaginationDto;
import com.mkhwang.gifticon.query.review.presentation.dto.QReviewDto_Review;
import com.mkhwang.gifticon.query.review.presentation.dto.QReviewDto_User;
import com.mkhwang.gifticon.query.review.presentation.dto.ReviewDto;
import com.mkhwang.gifticon.query.user.domain.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReviewQueryServiceImpl implements ReviewQueryService {
  private final JPAQueryFactory jpaQueryFactory;
  private final QReview qReview = QReview.review;
  private final QUser qUser = QUser.user;
  private final QuerydslUtil querydslUtil;

  @Override
  @Transactional(readOnly = true)
  public ReviewDto.ReviewPage searchReviews(String keyword, PaginationDto.PaginationRequest paginationRequest) {
    Pageable pageable = paginationRequest.toPageable();

    BooleanBuilder condition = this.buildSearchCondition(keyword);

    Long count = Optional.ofNullable(jpaQueryFactory.select(qReview.count()).from(qReview)
            .where(condition)
            .fetchOne()).orElse(0L);
    if (count == 0) {
      return ReviewDto.ReviewPage.builder()
              .items(List.of())
              .pagination(PaginationDto.PaginationInfo.builder()
                      .totalItems(0)
                      .totalPages(0)
                      .currentPage(paginationRequest.getPage())
                      .perPage(paginationRequest.getSize())
                      .build())
              .build();
    }

    List<ReviewDto.Review> result = jpaQueryFactory.select(
                    new QReviewDto_Review(
                            qReview.id,
                            new QReviewDto_User(
                                    qUser.id,
                                    qUser.nickname,
                                    qUser.profileImageUrl
                            ),
                            qReview.rating,
                            qReview.title,
                            qReview.content,
                            qReview.createdAt
                    )
            )
            .from(qReview)
            .join(qUser).on(qUser.id.eq(qReview.user.id))
            .where(condition)
            .orderBy(querydslUtil.getOrderSpecifiers(pageable, qReview.getType(), qReview.getMetadata().getName()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    return ReviewDto.ReviewPage.builder()
            .items(result)
            .pagination(PaginationDto.PaginationInfo.builder()
                    .totalItems(count.intValue())
                    .totalPages((int) Math.ceil((double) count / paginationRequest.getSize()))
                    .currentPage(paginationRequest.getPage())
                    .perPage(paginationRequest.getSize())
                    .build())
            .build();
  }

  private BooleanBuilder buildSearchCondition(String keyword) {
    BooleanBuilder builder = new BooleanBuilder();
    if (keyword != null && !keyword.isEmpty()) {
      builder.and(
              qReview.content.containsIgnoreCase(keyword)
                      .or(qReview.title.containsIgnoreCase(keyword))
                      .or(JPAExpressions
                              .selectOne()
                              .from(qUser)
                              .where(qUser.id.eq(qReview.user.id)
                                      .and(qUser.nickname.containsIgnoreCase(keyword)))
                              .exists())
      );
    }
    return builder;
  }
}
