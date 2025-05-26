package com.mkhwang.gifticon.command.review.application.usecase;

import com.mkhwang.gifticon.command.review.application.command.ReviewCommand;
import com.mkhwang.gifticon.command.review.presentation.dto.ReviewCreateResponse;

public interface CreateReviewUseCase {

  ReviewCreateResponse createReview(ReviewCommand.CreateReview command);

}
