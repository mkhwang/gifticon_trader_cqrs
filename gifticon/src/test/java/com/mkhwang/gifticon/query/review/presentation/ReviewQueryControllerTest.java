package com.mkhwang.gifticon.query.review.presentation;

import com.mkhwang.gifticon.common.dto.PaginationDto;
import com.mkhwang.gifticon.query.review.application.ReviewQueryService;
import com.mkhwang.gifticon.query.review.presentation.dto.ReviewDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReviewQueryControllerTest {
  @Mock
  ReviewQueryService reviewQueryService;
  @InjectMocks
  ReviewQueryController reviewQueryController;

  @DisplayName("리뷰 검색 API 테스트")
  @Test
  void getReviews() {
    // given
    String keyword = "test";
    int page = 1;
    int perPage = 10;
    String sort = "id:asc";
    ReviewDto.ReviewPage mockPage = mock(ReviewDto.ReviewPage.class);
    given(reviewQueryService.searchReviews(eq(keyword), any(PaginationDto.PaginationRequest.class))).willReturn(mockPage);

    // when
    var response = reviewQueryController.getReviews(keyword, page, perPage, sort);

    // then
    assertNotNull(response);
    verify(reviewQueryService).searchReviews(eq(keyword), any(PaginationDto.PaginationRequest.class));
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }
}