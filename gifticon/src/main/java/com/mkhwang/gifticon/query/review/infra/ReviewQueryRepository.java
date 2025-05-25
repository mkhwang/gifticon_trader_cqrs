package com.mkhwang.gifticon.query.review.infra;

import com.mkhwang.gifticon.query.review.presentation.dto.ReviewDto;

public interface ReviewQueryRepository {

  ReviewDto.ReviewSummary getUserReviewSummary(Long id);
}
