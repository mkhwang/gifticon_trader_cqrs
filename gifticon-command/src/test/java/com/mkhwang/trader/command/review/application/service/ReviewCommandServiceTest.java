package com.mkhwang.trader.command.review.application.service;

import com.mkhwang.trader.command.review.application.command.ReviewCommand;
import com.mkhwang.trader.command.review.presentation.mapper.ReviewDtoMapper;
import com.mkhwang.trader.common.gifticon.domain.Gifticon;
import com.mkhwang.trader.common.gifticon.domain.GifticonStatus;
import com.mkhwang.trader.common.gifticon.infra.GifticonRepository;
import com.mkhwang.trader.common.review.domain.Review;
import com.mkhwang.trader.common.review.infra.ReviewRepository;
import com.mkhwang.trader.common.user.domain.User;
import com.mkhwang.trader.common.user.infra.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewCommandServiceTest {
  @Mock
  ReviewRepository reviewRepository;
  @Mock
  GifticonRepository gifticonRepository;
  @Mock
  UserRepository userRepository;
  @Mock
  ReviewDtoMapper reviewDtoMapper;
  @InjectMocks
  ReviewCommandService reviewCommandService;


  @DisplayName("리뷰를 생성한다.")
  @Test
  void createReview() {
    // given
    ReviewCommand.CreateReview command = mock(ReviewCommand.CreateReview.class);
    Gifticon gifticon = new Gifticon();
    User buyer = mock(User.class);
    User seller = mock(User.class);
    Review review = mock(Review.class);

    gifticon.setSeller(seller);
    gifticon.setBuyer(buyer);
    gifticon.setStatus(GifticonStatus.SOLD_OUT);

    given(command.getGifticonId()).willReturn(1L);
    given(gifticonRepository.findById(command.getGifticonId())).willReturn(Optional.of(gifticon));
    given(command.getReviewerId()).willReturn(2L);
    given(command.getRating()).willReturn(3);
    given(command.getTitle()).willReturn("Awesome");
    given(command.getContent()).willReturn("Great gifticon!");
    given(userRepository.findById(command.getReviewerId())).willReturn(Optional.of(buyer));

    // then & then
    try (MockedStatic<Review> mocked = mockStatic(Review.class)) {
      mocked.when(() -> Review.of(gifticon, buyer, command.getRating(), command.getTitle(), command.getContent()))
              .thenReturn(review);

      when(reviewRepository.save(review)).thenReturn(review);

      reviewCommandService.createReview(command);

      verify(reviewRepository).save(review);
    }
  }
}