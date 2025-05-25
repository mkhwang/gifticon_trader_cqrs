package com.mkhwang.gifticon.query.user.application;

import com.mkhwang.gifticon.command.review.domain.QReview;
import com.mkhwang.gifticon.common.config.GenericMapper;
import com.mkhwang.gifticon.common.exception.ResourceNotFoundException;
import com.mkhwang.gifticon.command.review.infra.ReviewRepository;
import com.mkhwang.gifticon.query.user.infra.UserRepository;
import com.mkhwang.gifticon.common.dto.PaginationDto;
import com.mkhwang.gifticon.query.review.presentation.dto.ReviewDto;
import com.mkhwang.gifticon.query.user.presentation.dto.UserDto;
import com.mkhwang.gifticon.command.review.domain.Review;
import com.mkhwang.gifticon.query.user.domain.User;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final GenericMapper genericMapper;
  private final UserRepository userRepository;
  private final ReviewRepository reviewRepository;
  private final JPAQueryFactory jpaQueryFactory;
  private final QReview qReview = QReview.review;

  @Override
  @Transactional(readOnly = true)
  public UserDto.UserPage getAllUsers(String keyword, PaginationDto.PaginationRequest paginationRequest) {
    Page<User> result = userRepository.findByNicknameContainingIgnoreCase(keyword, paginationRequest.toPageable());

    return UserDto.UserPage.builder()
            .users(genericMapper.toDtoList(result.toList(), UserDto.User.class))
            .pagination(
                    PaginationDto.PaginationInfo.builder()
                            .totalItems((int) result.getTotalElements())
                            .totalPages(result.getTotalPages())
                            .currentPage(result.getNumber() + 1) // 0-based to 1-based
                            .perPage(result.getSize())
                            .build()
            )
            .build();
  }

  @Override
  @Transactional(readOnly = true)
  public ReviewDto.ReviewPage getUserReviews(Long id, PaginationDto.PaginationRequest paginationRequest) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", id));
    Pageable pageable = paginationRequest.toPageable();

    Page<Review> item = reviewRepository.findByUser(user, pageable);

    return ReviewDto.ReviewPage.builder()
            .items(genericMapper.toDtoList(item.toList(), ReviewDto.Review.class))
            .summary(this.getUserReviewSummary(id))
            .pagination(
                    PaginationDto.PaginationInfo.builder()
                            .totalItems((int) item.getTotalElements())
                            .totalPages(item.getTotalPages())
                            .currentPage(item.getNumber() + 1) // 0-based to 1-based
                            .perPage(item.getSize())
                            .build()
            )
            .build();
  }

  private ReviewDto.ReviewSummary getUserReviewSummary(Long id) {
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
      return null;
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
