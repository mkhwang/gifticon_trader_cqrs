package com.mkhwang.gifticon.service;

import com.mkhwang.gifticon.config.GenericMapper;
import com.mkhwang.gifticon.repository.UserRepository;
import com.mkhwang.gifticon.service.dto.PaginationDto;
import com.mkhwang.gifticon.service.dto.UserDto;
import com.mkhwang.gifticon.service.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final GenericMapper genericMapper;
  private final UserRepository userRepository;

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
}
