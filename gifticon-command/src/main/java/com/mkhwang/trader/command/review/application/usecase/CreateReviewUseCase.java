package com.mkhwang.trader.command.review.application.usecase;

import com.mkhwang.trader.command.review.application.command.ReviewCommand;
import com.mkhwang.trader.command.review.presentation.dto.ReviewCreateResponse;

public interface CreateReviewUseCase {

  ReviewCreateResponse createReview(ReviewCommand.CreateReview command);

}
