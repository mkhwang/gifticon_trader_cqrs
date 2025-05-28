package com.mkhwang.trader.command.review.application.service;

import com.mkhwang.trader.common.exception.ResourceNotFoundException;
import com.mkhwang.trader.common.gifticon.domain.Gifticon;
import com.mkhwang.trader.common.gifticon.infra.GifticonRepository;
import com.mkhwang.trader.command.review.application.command.ReviewCommand;
import com.mkhwang.trader.command.review.application.usecase.CreateReviewUseCase;
import com.mkhwang.trader.common.review.domain.Review;
import com.mkhwang.trader.common.review.infra.ReviewRepository;
import com.mkhwang.trader.command.review.presentation.dto.ReviewCreateResponse;
import com.mkhwang.trader.command.review.presentation.mapper.ReviewDtoMapper;
import com.mkhwang.trader.common.user.domain.User;
import com.mkhwang.trader.common.user.infra.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReviewCommandService implements CreateReviewUseCase {
  private final ReviewRepository reviewRepository;
  private final GifticonRepository gifticonRepository;
  private final UserRepository userRepository;
  private final ReviewDtoMapper reviewDtoMapper;

  @Override
  @Transactional
  public ReviewCreateResponse createReview(ReviewCommand.CreateReview command) {
    Gifticon gifticon = gifticonRepository.findById(command.getGifticonId()).orElseThrow(
            () -> new ResourceNotFoundException("Gifticon", command.getGifticonId())
    );
    User user = userRepository.findById((command.getReviewerId()))
            .orElseThrow(() -> new ResourceNotFoundException("User", command.getReviewerId()));

    Review review = Review.of(gifticon, user, command.getRating(), command.getTitle(), command.getContent());

    review = reviewRepository.save(review);

    return reviewDtoMapper.toReviewCreateResponse(review);
  }
}
