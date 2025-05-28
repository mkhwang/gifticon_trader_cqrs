package com.mkhwang.trader.query.review.presentation;

import com.mkhwang.trader.common.dto.ApiResponse;
import com.mkhwang.trader.common.dto.PaginationDto;
import com.mkhwang.trader.query.review.application.ReviewQueryService;
import com.mkhwang.trader.query.review.presentation.dto.ReviewDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewQueryController {
  private final ReviewQueryService reviewQueryService;

  @Operation(summary = "Review 검색", description = "Review 검색 API")
  @GetMapping("/api/reviews")
  public ResponseEntity<?> getReviews(
          @RequestParam(required = false) String keyword,
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "10") int perPage,
          @RequestParam(defaultValue = "id:asc") String sort
  ) {
    PaginationDto.PaginationRequest paginationRequest = PaginationDto.PaginationRequest.builder()
            .page(page)
            .size(perPage)
            .sort(sort)
            .build();

    ReviewDto.ReviewPage reviewPage = reviewQueryService.searchReviews(keyword, paginationRequest);
    return ResponseEntity.ok(ApiResponse.builder().data(reviewPage).build());
  }
}
