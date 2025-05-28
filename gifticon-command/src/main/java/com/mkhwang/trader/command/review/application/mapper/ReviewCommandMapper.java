package com.mkhwang.trader.command.review.application.mapper;

import com.mkhwang.trader.command.review.application.command.ReviewCommand;
import com.mkhwang.trader.command.review.presentation.dto.ReviewCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class ReviewCommandMapper {

  public ReviewCommand.CreateReview toCreateReviewCommand(
          ReviewCreateRequest request) {
    return ReviewCommand.CreateReview.builder()
            .gifticonId(request.getGifticonId())
            .reviewerId(request.getReviewerId())
            .content(request.getContent())
            .rating(request.getRating())
            .build();
  }
}
