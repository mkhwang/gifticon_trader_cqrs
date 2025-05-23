package com.mkhwang.gifticon.service;

import com.mkhwang.gifticon.service.dto.PaginationDto;
import com.mkhwang.gifticon.service.dto.UserDto;

import java.util.List;

public interface UserService {
  UserDto.UserPage getAllUsers(String keyword, PaginationDto.PaginationRequest paginationRequest);
}
