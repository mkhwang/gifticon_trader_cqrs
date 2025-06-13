package com.mkhwang.trader.common.gifticon.domain;

import com.mkhwang.trader.common.brand.domain.Brand;
import com.mkhwang.trader.common.category.domain.Category;
import com.mkhwang.trader.common.tag.domain.Tag;
import com.mkhwang.trader.common.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GifticonTest {
  private User seller;
  private User buyer;
  private Gifticon gifticon;

  @BeforeEach
  void setUp() {
    seller = User.of("user", "nickname", "image.png");
    seller.setId(1L);

    buyer = User.of("buyer", "buyer", "image.png");
    buyer.setId(2L);

    gifticon = new Gifticon();
    gifticon.setName("test");
    gifticon.setDescription("description");
    gifticon.setStatus(GifticonStatus.ON_SALE);
    gifticon.setPrice(GifticonPrice.builder().basePrice(BigDecimal.ZERO).salePrice(BigDecimal.ZERO).build());
    gifticon.setCategory(Category.builder().id(1L).name("category").slug("category").level(1).build());
    User user = User.of("user", "user", "image.png");
    user.setId(1L);
    gifticon.setSeller(user);
    Brand brand = Brand.builder().name("brand").slug("brand").build();
    brand.setId(1L);
    gifticon.setBrand(brand);
    gifticon.setTags(List.of(Tag.builder().name("tag").slug("tag").build()));
    gifticon.setSlug("test");
  }

  @DisplayName("Gifticon이 소유자인지 확인하는 테스트")
  @Test
  void isOwnedBy() {
    // Given
    gifticon.setSeller(seller);

    // When
    boolean result = gifticon.isOwnedBy(seller.getId());

    // Then
    assertTrue(result);
  }

  @DisplayName("Gifticon이 거래 가능한지 확인하는 테스트")
  @Test
  void isTradeAble() {
    // Given
    Gifticon gifticon1 = new Gifticon();
    Gifticon gifticon2 = new Gifticon();
    gifticon1.setStatus(GifticonStatus.ON_SALE);
    gifticon2.setStatus(GifticonStatus.SOLD_OUT);

    // When
    boolean result = gifticon1.isTradeAble();

    // Then
    assertTrue(result);
    assertFalse(gifticon2.isTradeAble());
  }

  @DisplayName("Gifticon이 리뷰 가능한지 확인하는 테스트")
  @Test
  void isReviewable() {
    // Given
    gifticon.setStatus(GifticonStatus.SOLD_OUT);
    gifticon.setBuyer(buyer);

    // When
    boolean result = gifticon.isReviewable();

    // Then
    assertTrue(result);

  }

  @DisplayName("Gifticon이 삭제 가능한지 확인하는 테스트")
  @Test
  void isDeleteAble() {
    // Given
    Gifticon gifticon1 = new Gifticon();
    gifticon1.setStatus(GifticonStatus.ON_SALE);

    Gifticon gifticon2 = new Gifticon();
    gifticon2.setStatus(GifticonStatus.SOLD_OUT);

    // When
    boolean result = gifticon1.isDeleteAble();

    // Then
    assertTrue(result);
    assertFalse(gifticon2.isDeleteAble());
  }

  @DisplayName("Gifticon 구매 테스트")
  @Test
  void buy() {
    // Given
    Gifticon gifticon = new Gifticon();
    gifticon.setSeller(seller);
    gifticon.setStatus(GifticonStatus.ON_SALE);

    // When
    gifticon.buy(buyer);

    // Then
    assertEquals(GifticonStatus.SOLD_OUT, gifticon.getStatus());
    assertEquals(buyer, gifticon.getBuyer());
  }

  @DisplayName("Gifticon 구매 시 소유자가 구매할 수 없는 테스트")
  @Test
  void buy_whenSellerTriesToBuy() {
    // Given
    Gifticon gifticon = new Gifticon();
    gifticon.setSeller(seller);
    gifticon.setStatus(GifticonStatus.SOLD_OUT);

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> gifticon.buy(seller));
  }

  @DisplayName("Gifticon 삭제 테스트")
  @Test
  void delete() {
    // Given
    Gifticon gifticon = new Gifticon();
    gifticon.setSeller(seller);
    gifticon.setStatus(GifticonStatus.ON_SALE);

    // When
    gifticon.delete(seller.getId());

    // Then
    assertEquals(GifticonStatus.DELETED, gifticon.getStatus());
  }
}