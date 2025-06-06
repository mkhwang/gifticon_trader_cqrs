package com.mkhwang.trader.query.user.application;

import com.mkhwang.trader.common.config.GenericMapper;
import com.mkhwang.trader.common.dto.PaginationDto;
import com.mkhwang.trader.common.exception.ResourceNotFoundException;
import com.mkhwang.trader.common.review.domain.Review;
import com.mkhwang.trader.common.review.infra.ReviewRepository;
import com.mkhwang.trader.common.user.domain.User;
import com.mkhwang.trader.common.user.infra.UserRepository;
import com.mkhwang.trader.query.review.infra.ReviewQueryRepository;
import com.mkhwang.trader.query.review.presentation.dto.ReviewDto;
import com.mkhwang.trader.query.user.presentation.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final GenericMapper genericMapper;
  private final UserRepository userRepository;
  private final ReviewRepository reviewRepository;
  private final ReviewQueryRepository reviewQueryRepository;

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

  @Override
  @Transactional(readOnly = true)
  public ReviewDto.ReviewPage getUserReviews(Long id, PaginationDto.PaginationRequest paginationRequest) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User", id));
    Pageable pageable = paginationRequest.toPageable();

    Page<Review> item = reviewRepository.findByUser(user, pageable);

    return ReviewDto.ReviewPage.builder()
            .items(genericMapper.toDtoList(item.toList(), ReviewDto.Review.class))
            .summary(reviewQueryRepository.getUserReviewSummary(id))
            .pagination(
                    PaginationDto.PaginationInfo.builder()
                            .totalItems((int) item.getTotalElements())
                            .totalPages(item.getTotalPages())
                            .currentPage(item.getNumber() + 1) // 0-based to 1-based
                            .perPage(item.getSize())
                            .build()
            )
            .build();
  }
}
