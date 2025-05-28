package com.mkhwang.trader.query.review.infra;

import com.mkhwang.trader.query.review.presentation.dto.ReviewDto;

public interface ReviewQueryRepository {

  ReviewDto.ReviewSummary getUserReviewSummary(Long id);
}
