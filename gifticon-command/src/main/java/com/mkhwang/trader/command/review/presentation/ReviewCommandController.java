package com.mkhwang.trader.command.review.presentation;

import com.mkhwang.trader.command.review.application.command.ReviewCommand;
import com.mkhwang.trader.command.review.application.mapper.ReviewCommandMapper;
import com.mkhwang.trader.command.review.application.usecase.CreateReviewUseCase;
import com.mkhwang.trader.command.review.presentation.dto.ReviewCreateRequest;
import com.mkhwang.trader.command.review.presentation.dto.ReviewCreateResponse;
import com.mkhwang.trader.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewCommandController {
  private final ReviewCommandMapper reviewCommandMapper;
  private final CreateReviewUseCase createReviewUseCase;

  @PostMapping("/api/review")
  public ResponseEntity<?> createReview(ReviewCreateRequest request) {
    ReviewCommand.CreateReview command = reviewCommandMapper.toCreateReviewCommand(request);
    ReviewCreateResponse review = createReviewUseCase.createReview(command);
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.builder().data(review).success(true).build());
  }
}
