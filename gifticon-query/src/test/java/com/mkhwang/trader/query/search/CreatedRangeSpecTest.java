package com.mkhwang.trader.query.search;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import com.mkhwang.trader.query.gifticon.application.query.GifticonQuery;
import com.mkhwang.trader.query.gifticon.application.search.CreatedRangeSpec;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class CreatedRangeSpecTest {
  private final CreatedRangeSpec createdRangeSpec = new CreatedRangeSpec();

  @DisplayName("CreatedRange 조건 검색 테스트")
  @Test
  void createdRangeSpecTest() {
    // given
    LocalDate from = LocalDate.of(2025, 1, 1);
    LocalDate to = LocalDate.of(2025, 1, 31);
    GifticonQuery.ListGifticons query = GifticonQuery.ListGifticons.builder()
            .createdFrom(from)
            .createdTo(to)
            .build();

    // when
    Query result = createdRangeSpec.build(query);


    // then
    assertNotNull(result);
    RangeQuery range = result.range();
    assertEquals("createdAt", range.date().field());
    assertEquals(from.format(DateTimeFormatter.ISO_DATE), range.date().gte());
    assertEquals(to.format(DateTimeFormatter.ISO_DATE), range.date().lte());
  }

  @DisplayName("CreatedRange 조건 검색이 없을 때 null 반환 테스트")
  @Test
  void createdRangeSpecNullTest() {
    // given
    GifticonQuery.ListGifticons query = GifticonQuery.ListGifticons.builder()
            .createdFrom(null)
            .createdTo(null)
            .build();

    // when
    Query result = createdRangeSpec.build(query);

    // then
    assertNull(result);
  }

}