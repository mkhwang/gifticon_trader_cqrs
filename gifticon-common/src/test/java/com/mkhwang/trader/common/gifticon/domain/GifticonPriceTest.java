package com.mkhwang.trader.common.gifticon.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GifticonPriceTest {

  @DisplayName("할인 가격 계산 테스트")
  @Test
  void getDiscountPercentage() {
    // Given
    BigDecimal basePrice = new BigDecimal("10000");
    BigDecimal salePrice = new BigDecimal("8000");
    GifticonPrice gifticonPrice = new GifticonPrice();
    gifticonPrice.setBasePrice(basePrice);
    gifticonPrice.setSalePrice(salePrice);

    // When
    double discountPercentage = gifticonPrice.getDiscountPercentage();

    // Then
    assertEquals(20.0, discountPercentage, 0.01);
  }

  @DisplayName("할인 가격 계산 테스트")
  @Test
  void discountPercentage_should_return_basePrice_is_null_or_zero() {
    // Given
    BigDecimal zero = BigDecimal.ZERO;
    BigDecimal price = new BigDecimal("8000");
    GifticonPrice gifticonPrice1 = new GifticonPrice();
    GifticonPrice gifticonPrice2 = new GifticonPrice();
    GifticonPrice gifticonPrice3 = new GifticonPrice();
    GifticonPrice gifticonPrice4 = new GifticonPrice();
    gifticonPrice1.setBasePrice(zero);
    gifticonPrice1.setSalePrice(price);

    gifticonPrice2.setBasePrice(null);
    gifticonPrice2.setSalePrice(price);

    gifticonPrice3.setBasePrice(price);
    gifticonPrice3.setSalePrice(null);

    // When
    double discountPercentage1 = gifticonPrice1.getDiscountPercentage();
    double discountPercentage2 = gifticonPrice2.getDiscountPercentage();
    double discountPercentage3 = gifticonPrice3.getDiscountPercentage();

    // Then
    assertEquals(0D, discountPercentage1);
    assertEquals(0D, discountPercentage2);
    assertEquals(0D, discountPercentage3);
  }
}