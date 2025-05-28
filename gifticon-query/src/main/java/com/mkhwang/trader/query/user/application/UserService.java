package com.mkhwang.trader.query.user.application;

import com.mkhwang.trader.common.dto.PaginationDto;
import com.mkhwang.trader.query.review.presentation.dto.ReviewDto;
import com.mkhwang.trader.query.user.presentation.dto.UserDto;

public interface UserService {
  UserDto.UserPage getAllUsers(String keyword, PaginationDto.PaginationRequest paginationRequest);

  ReviewDto.ReviewPage getUserReviews(Long id, PaginationDto.PaginationRequest paginationRequest);
}
