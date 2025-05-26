package com.mkhwang.gifticon.command.review.presentation.mapper;

import com.mkhwang.gifticon.command.gifticon.domain.Gifticon;
import com.mkhwang.gifticon.command.review.domain.Review;
import com.mkhwang.gifticon.query.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ReviewDtoMapperTest {
  private final ReviewDtoMapper reviewDtoMapper = new ReviewDtoMapper();

  @DisplayName("ReviewDtoMapper toReviewCreateResponse 테스트")
  @Test
  void toReviewCreateResponse() {
    // Given
    Long id = 1L;
    Long gifticonId = 2L;
    String title = "Great Gifticon!";
    String content = "Loved it!";
    int rating = 5;
    Gifticon gifticon = new Gifticon();
    gifticon.setId(gifticonId);
    User reviewer = mock(User.class);
    User user = mock(User.class);

    // When
    var response = reviewDtoMapper.toReviewCreateResponse(
        new Review(id, gifticon, user, reviewer, rating, title, content)
    );

    // Then
    assertNotNull(response);
    assertEquals(id, response.getId());
    assertEquals(title, response.getTitle());
    assertEquals(content, response.getContent());
    assertEquals(rating, response.getRating());
  }
}