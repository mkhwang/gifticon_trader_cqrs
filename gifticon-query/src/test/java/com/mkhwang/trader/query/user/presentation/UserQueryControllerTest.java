package com.mkhwang.trader.query.user.presentation;

import com.mkhwang.trader.common.dto.PaginationDto;
import com.mkhwang.trader.query.review.presentation.dto.ReviewDto;
import com.mkhwang.trader.query.user.application.UserService;
import com.mkhwang.trader.query.user.presentation.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserQueryControllerTest {
  @Mock
  UserService userService;
  @InjectMocks
  UserQueryController userQueryController;

  @Test
  void getAllUsers() {
    // given
    String keyword = "test";
    int page = 1;
    int perPage = 10;
    String sort = "id:asc";

    UserDto.UserPage mockPage = mock(UserDto.UserPage.class);
    given(userService.getAllUsers(eq(keyword), any(PaginationDto.PaginationRequest.class)))
            .willReturn(mockPage);

    // when
    ResponseEntity<?> response = userQueryController.getAllUsers(keyword, page, perPage, sort);

    // then
    verify(userService).getAllUsers(eq(keyword), any(PaginationDto.PaginationRequest.class));
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

  }

  @Test
  void getUserReviews() {
    // given
    Long userId = 1L;
    int page = 1;
    int perPage = 10;
    String sort = "createdAt:desc";
    ReviewDto.ReviewPage mockReviewPage = mock(ReviewDto.ReviewPage.class);
    given(userService.getUserReviews(eq(userId), any(PaginationDto.PaginationRequest.class))).willReturn(mockReviewPage);

    // when
    ResponseEntity<?> response = userQueryController.getUserReviews(userId, page, perPage, sort);

    // then
    verify(userService).getUserReviews(eq(userId), any(PaginationDto.PaginationRequest.class));
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }
}