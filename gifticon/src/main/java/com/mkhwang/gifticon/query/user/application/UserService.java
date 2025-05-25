package com.mkhwang.gifticon.query.user.application;

import com.mkhwang.gifticon.common.dto.PaginationDto;
import com.mkhwang.gifticon.query.review.presentation.dto.ReviewDto;
import com.mkhwang.gifticon.query.user.presentation.dto.UserDto;

public interface UserService {
  UserDto.UserPage getAllUsers(String keyword, PaginationDto.PaginationRequest paginationRequest);

  ReviewDto.ReviewPage getUserReviews(Long id, PaginationDto.PaginationRequest paginationRequest);
}
