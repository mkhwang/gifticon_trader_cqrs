package com.mkhwang.trader.query.user.application;

import com.mkhwang.trader.common.config.GenericMapper;
import com.mkhwang.trader.common.dto.PaginationDto;
import com.mkhwang.trader.common.review.infra.ReviewRepository;
import com.mkhwang.trader.common.user.domain.User;
import com.mkhwang.trader.common.user.infra.UserRepository;
import com.mkhwang.trader.query.review.infra.ReviewQueryRepository;
import com.mkhwang.trader.query.review.presentation.dto.ReviewDto;
import com.mkhwang.trader.query.user.presentation.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock
  GenericMapper genericMapper;
  @Mock
  UserRepository userRepository;
  @Mock
  ReviewRepository reviewRepository;
  @Mock
  ReviewQueryRepository reviewQueryRepository;
  @InjectMocks
  UserServiceImpl userService;

  @Test
  void getAllUsers() {
    // given
    String keyword = "test";
    PaginationDto.PaginationRequest page = mock(PaginationDto.PaginationRequest.class);
    Pageable pageable = mock(Pageable.class);
    Page<User> result = mock(Page.class);
    given(page.toPageable()).willReturn(pageable);
    given(userRepository.findByNicknameContainingIgnoreCase(keyword, pageable)).willReturn(result);

    // when
    UserDto.UserPage allUsers = userService.getAllUsers(keyword, page);

    // then
    verify(userRepository).findByNicknameContainingIgnoreCase(keyword, pageable);
    verify(genericMapper).toDtoList(result.toList(), UserDto.User.class);
  }

  @Test
  void getUserReviews() {
    // given
    Long id = 1L;
    PaginationDto.PaginationRequest page = mock(PaginationDto.PaginationRequest.class);
    User user = mock(User.class);
    Pageable pageable = mock(Pageable.class);
    Page mockPage = mock(Page.class);
    List<ReviewDto.Review> reviewList = mock(List.class);
    given(page.toPageable()).willReturn(pageable);
    given(userRepository.findById(id)).willReturn(Optional.of(user));
    given(reviewRepository.findByUser(user , pageable)).willReturn(mockPage);
    given(mockPage.toList()).willReturn(reviewList);

    // when
    ReviewDto.ReviewPage userReviews = userService.getUserReviews(id, page);

    // then
    verify(userRepository).findById(id);
    verify(reviewRepository).findByUser(user, pageable);
    verify(genericMapper).toDtoList(mockPage.toList(), ReviewDto.Review.class);
    verify(reviewQueryRepository).getUserReviewSummary(id);
  }
}