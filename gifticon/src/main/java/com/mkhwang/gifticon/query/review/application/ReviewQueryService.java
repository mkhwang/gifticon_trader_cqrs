package com.mkhwang.gifticon.query.review.application;

import com.mkhwang.gifticon.common.dto.PaginationDto;
import com.mkhwang.gifticon.query.review.presentation.dto.ReviewDto;

public interface ReviewQueryService {

  ReviewDto.ReviewPage searchReviews(String keyword, PaginationDto.PaginationRequest paginationRequest);
}
