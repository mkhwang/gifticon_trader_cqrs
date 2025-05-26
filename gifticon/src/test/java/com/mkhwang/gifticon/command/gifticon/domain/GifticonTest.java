package com.mkhwang.gifticon.command.gifticon.domain;

import com.mkhwang.gifticon.query.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GifticonTest {

  @DisplayName("Gifticon이 소유자인지 확인하는 테스트")
  @Test
  void isOwnedBy() {
    // Given
    Gifticon gifticon = new Gifticon();
    User user = mock(User.class);
    given(user.getId()).willReturn(1L);
    Long userId = 1L;
    gifticon.setSeller(user);

    // When
    boolean result = gifticon.isOwnedBy(userId);

    // Then
    assertTrue(result);
  }

  @DisplayName("Gifticon이 거래 가능한지 확인하는 테스트")
  @Test
  void isTradeAble() {
    // Given
    Gifticon gifticon = new Gifticon();
    Gifticon gifticon2 = new Gifticon();
    gifticon.setStatus(GifticonStatus.ON_SALE);
    gifticon2.setStatus(GifticonStatus.SOLD_OUT);

    // When
    boolean result = gifticon.isTradeAble();

    // Then
    assertTrue(result);
    assertFalse(gifticon2.isTradeAble());
  }

  @DisplayName("Gifticon이 리뷰 가능한지 확인하는 테스트")
  @Test
  void isReviewable() {
    // Given
    Gifticon gifticon = new Gifticon();
    User user = mock(User.class);
    gifticon.setStatus(GifticonStatus.SOLD_OUT);
    gifticon.setBuyer(user);

    // When
    boolean result = gifticon.isReviewable();

    // Then
    assertTrue(result);

  }

  @DisplayName("Gifticon이 삭제 가능한지 확인하는 테스트")
  @Test
  void isDeleteAble() {
    // Given
    Gifticon gifticon = new Gifticon();
    gifticon.setStatus(GifticonStatus.ON_SALE);

    Gifticon gifticon2 = new Gifticon();
    gifticon2.setStatus(GifticonStatus.SOLD_OUT);

    // When
    boolean result = gifticon.isDeleteAble();

    // Then
    assertTrue(result);
    assertFalse(gifticon2.isDeleteAble());
  }

  @DisplayName("Gifticon 구매 테스트")
  @Test
  void buy() {
    // Given
    Gifticon gifticon = new Gifticon();
    User user = mock(User.class);
    given(user.getId()).willReturn(2L);
    gifticon.setSeller(mock(User.class));
    gifticon.setStatus(GifticonStatus.ON_SALE);

    // When
    gifticon.buy(user);

    // Then
    assertEquals(GifticonStatus.SOLD_OUT, gifticon.getStatus());
    assertEquals(user, gifticon.getBuyer());
  }

  @DisplayName("Gifticon 구매 시 소유자가 구매할 수 없는 테스트")
  @Test
  void buy_whenSellerTriesToBuy() {
    // Given
    Gifticon gifticon = new Gifticon();
    User user = mock(User.class);
    given(user.getId()).willReturn(1L);
    gifticon.setSeller(user);
    gifticon.setStatus(GifticonStatus.ON_SALE);

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> gifticon.buy(user));
  }

  @DisplayName("Gifticon 삭제 테스트")
  @Test
  void delete() {
    // Given
    Gifticon gifticon = new Gifticon();
    User user = mock(User.class);
    given(user.getId()).willReturn(1L);
    gifticon.setSeller(user);
    gifticon.setStatus(GifticonStatus.ON_SALE);

    // When
    gifticon.delete(user.getId());

    // Then
    assertEquals(GifticonStatus.DELETED, gifticon.getStatus());
  }
}