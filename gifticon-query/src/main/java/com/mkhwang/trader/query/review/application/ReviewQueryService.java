package com.mkhwang.trader.query.review.application;

import com.mkhwang.trader.common.dto.PaginationDto;
import com.mkhwang.trader.query.review.presentation.dto.ReviewDto;

public interface ReviewQueryService {

  ReviewDto.ReviewPage searchReviews(String keyword, PaginationDto.PaginationRequest paginationRequest);
}
