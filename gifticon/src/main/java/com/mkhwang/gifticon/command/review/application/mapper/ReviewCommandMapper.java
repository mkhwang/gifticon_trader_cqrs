package com.mkhwang.gifticon.command.review.application.mapper;

import com.mkhwang.gifticon.command.gifticon.domain.Gifticon;
import com.mkhwang.gifticon.command.review.application.command.ReviewCommand;
import com.mkhwang.gifticon.command.review.domain.Review;
import com.mkhwang.gifticon.command.review.presentation.dto.ReviewCreateRequest;
import com.mkhwang.gifticon.query.user.domain.User;
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

  public Review toReview(ReviewCommand.CreateReview command, Gifticon gifticon, User reviewer) {
    return Review.builder()
            .gifticon(gifticon)
            .user(gifticon.getSeller())
            .reviewer(reviewer)
            .content(command.getContent())
            .rating(command.getRating())
            .build();
  }
}
