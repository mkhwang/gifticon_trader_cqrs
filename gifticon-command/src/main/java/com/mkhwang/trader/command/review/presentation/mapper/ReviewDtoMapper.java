package com.mkhwang.trader.command.review.presentation.mapper;

import com.mkhwang.trader.common.review.domain.Review;
import com.mkhwang.trader.command.review.presentation.dto.ReviewCreateResponse;
import org.springframework.stereotype.Component;

@Component
public class ReviewDtoMapper {
  public ReviewCreateResponse toReviewCreateResponse(Review review) {
    return ReviewCreateResponse.builder()
        .id(review.getId())
        .gifticonId(review.getGifticon().getId())
        .reviewerId(review.getReviewer().getId())
        .title(review.getTitle())
        .content(review.getContent())
        .rating(review.getRating())
        .build();
  }
}
