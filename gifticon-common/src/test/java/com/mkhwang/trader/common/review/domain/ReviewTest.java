package com.mkhwang.trader.common.review.domain;

import com.mkhwang.trader.common.gifticon.domain.Gifticon;
import com.mkhwang.trader.common.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class ReviewTest {

  @Test
  @DisplayName("정상적인 조건에서 Review.of()는 Review 객체를 생성한다")
  void of_shouldCreateReview_whenConditionsAreValid() {
    // given
    User seller = mock(User.class);
    User buyer = mock(User.class);
    Gifticon gifticon = mock(Gifticon.class);

    given(gifticon.getSeller()).willReturn(seller);
    given(gifticon.getBuyer()).willReturn(buyer);
    given(gifticon.isReviewable()).willReturn(true);
    given(gifticon.isOwnedBy(buyer.getId())).willReturn(false);

    // when
    Review review = Review.of(gifticon, buyer, 5, "만족합니다", "정말 빠르게 도착했어요!");

    // then
    assertEquals(gifticon, review.getGifticon());
    assertEquals(seller, review.getUser());
    assertEquals(buyer, review.getReviewer());
    assertEquals(5, review.getRating());
    assertEquals("만족합니다", review.getTitle());
    assertEquals("정말 빠르게 도착했어요!", review.getContent());
  }

  @Test
  @DisplayName("Gifticon이 리뷰 가능한 상태가 아니면 예외가 발생한다")
  void of_shouldThrowException_whenGifticonIsNotReviewable() {
    // given
    User reviewer = mock(User.class);
    Gifticon gifticon = mock(Gifticon.class);

    given(gifticon.isReviewable()).willReturn(false);

    // when & then
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            Review.of(gifticon, reviewer, 5, "title", "content")
    );
  }

  @Test
  @DisplayName("Reviewer가 해당 기프티콘의 소유자이면 예외가 발생한다")
  void of_shouldThrowException_whenReviewerOwnsGifticon() {
    // given
    User reviewer = mock(User.class);
    Gifticon gifticon = mock(Gifticon.class);

    given(gifticon.isReviewable()).willReturn(true);
    given(gifticon.isOwnedBy(reviewer.getId())).willReturn(true);

    // when & then
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            Review.of(gifticon, reviewer, 4, "제목", "내용")
    );
  }

  @Test
  @DisplayName("Reviewer가 기프티콘의 구매자가 아니면 예외가 발생한다")
  void of_shouldThrowException_whenReviewerIsNotBuyer() {
    // given
    User reviewer = mock(User.class);
    User actualBuyer = mock(User.class);
    Gifticon gifticon = mock(Gifticon.class);

    given(gifticon.isReviewable()).willReturn(true);
    given(gifticon.isOwnedBy(reviewer.getId())).willReturn(false);
    given(gifticon.getBuyer()).willReturn(actualBuyer);

    // when & then
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            Review.of(gifticon, reviewer, 4, "제목", "내용")
    );
  }
}