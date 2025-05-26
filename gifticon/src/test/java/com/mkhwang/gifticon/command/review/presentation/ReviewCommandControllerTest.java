package com.mkhwang.gifticon.command.review.presentation;

import com.mkhwang.gifticon.command.review.application.mapper.ReviewCommandMapper;
import com.mkhwang.gifticon.command.review.application.usecase.CreateReviewUseCase;
import com.mkhwang.gifticon.command.review.presentation.dto.ReviewCreateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReviewCommandControllerTest {
  @Mock
  ReviewCommandMapper reviewCommandMapper;
  @Mock
  CreateReviewUseCase createReviewUseCase;
  @InjectMocks
  ReviewCommandController reviewCommandController;

  @Test
  void createReview() {
    // Given
    ReviewCreateRequest request = new ReviewCreateRequest();
    request.setGifticonId(1L);
    request.setReviewerId(2L);
    request.setRating(5);
    request.setTitle("Great Gifticon!");
    request.setContent("Loved it!");

    // When
    var response = reviewCommandController.createReview(request);

    // Then
    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
  }
}