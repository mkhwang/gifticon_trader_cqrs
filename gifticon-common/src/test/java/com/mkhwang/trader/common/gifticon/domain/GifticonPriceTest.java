package com.mkhwang.trader.common.gifticon.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GifticonPriceTest {

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
}