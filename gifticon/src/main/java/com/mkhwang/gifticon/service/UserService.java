package com.mkhwang.gifticon.service;

import com.mkhwang.gifticon.service.dto.PaginationDto;
import com.mkhwang.gifticon.service.dto.ReviewDto;
import com.mkhwang.gifticon.service.dto.UserDto;

public interface UserService {
  UserDto.UserPage getAllUsers(String keyword, PaginationDto.PaginationRequest paginationRequest);

  ReviewDto.ReviewPage getUserReviews(Long id, PaginationDto.PaginationRequest paginationRequest);
}
