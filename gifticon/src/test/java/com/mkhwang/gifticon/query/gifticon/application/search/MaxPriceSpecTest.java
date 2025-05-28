package com.mkhwang.gifticon.query.gifticon.application.search;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.mkhwang.gifticon.query.gifticon.presentation.dto.GifticonQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MaxPriceSpecTest {
  private final MaxPriceSpec maxPriceSpec = new MaxPriceSpec();

  @DisplayName("최대 가격 조건 검색 테스트")
  @Test
  void maxPriceSpecTest() {
    // given
    BigDecimal maxPrice = BigDecimal.valueOf(1000);
    GifticonQuery.ListGifticons query = GifticonQuery.ListGifticons.builder()
            .maxPrice(maxPrice)
            .build();

    // when
    Query result = maxPriceSpec.build(query);

    // then
    assertNotNull(result.range());
    assertEquals(result.range().number().field(), "salePrice");
    assertEquals(result.range().number().lte(), maxPrice.intValue());
  }
}