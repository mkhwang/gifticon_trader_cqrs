package com.mkhwang.trader.command.review.application.mapper;

import com.mkhwang.trader.command.review.application.command.ReviewCommand;
import com.mkhwang.trader.command.review.presentation.dto.ReviewCreateRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReviewCommandMapperTest {
  private final ReviewCommandMapper reviewCommandMapper = new ReviewCommandMapper();

  @Test
  void toCreateReviewCommand() {
    // given
    ReviewCreateRequest request = new ReviewCreateRequest();
    request.setGifticonId(1L);
    request.setReviewerId(2L);
    request.setContent("Great gifticon!");
    request.setRating(5);


    // when
    ReviewCommand.CreateReview createReviewCommand = reviewCommandMapper.toCreateReviewCommand(request);

    // then
    assertNotNull(createReviewCommand);
    assertEquals(1L, createReviewCommand.getGifticonId());
    assertEquals(2L, createReviewCommand.getReviewerId());
    assertEquals("Great gifticon!", createReviewCommand.getContent());
    assertEquals(5, createReviewCommand.getRating());
  }

  @Test
  void toReview() {
  }
}