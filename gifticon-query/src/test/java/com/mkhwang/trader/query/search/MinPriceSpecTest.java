package com.mkhwang.trader.query.search;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.mkhwang.trader.query.gifticon.application.query.GifticonQuery;
import com.mkhwang.trader.query.gifticon.application.search.MinPriceSpec;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MinPriceSpecTest {
  private final MinPriceSpec minPriceSpec = new MinPriceSpec();

  @DisplayName("최소 가격 조건 검색 테스트")
  @Test
  void minPriceSpecTest() {
    // given
    BigDecimal minPrice = BigDecimal.valueOf(100);
    GifticonQuery.ListGifticons query = GifticonQuery.ListGifticons.builder()
            .minPrice(minPrice)
            .build();

    // when
    Query result = minPriceSpec.build(query);

    // then
    assertNotNull(result.range());
    assertEquals(result.range().number().field(), "salePrice");
    assertEquals(result.range().number().gte(), minPrice.intValue());
  }
}