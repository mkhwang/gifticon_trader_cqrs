package com.mkhwang.trader.query.gifticon.presentation.mapper;

import com.mkhwang.trader.query.gifticon.application.query.GifticonQuery;
import com.mkhwang.trader.query.gifticon.presentation.dto.GifticonListRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GiftionQueryMapperTest {
  private final GiftionQueryMapper mapper = new GiftionQueryMapper();

  @Test
  void toGifticonListQuery_shouldMapCorrectly() {
    // given
    GifticonListRequest request = new GifticonListRequest();
    request.setStatus("ACTIVE");
    request.setMinPrice(BigDecimal.valueOf(1000));
    request.setMaxPrice(BigDecimal.valueOf(5000));
    request.setCategory(List.of(1L));
    request.setSeller(2L);
    request.setBrand(3L);
    request.setSearch("아메리카노");
    request.setCreatedFrom(LocalDate.of(2024, 1, 1));
    request.setCreatedTo(LocalDate.of(2024, 12, 31));
    request.setPage(2);
    request.setPerPage(20);
    request.setSort("price:asc");

    // when
    GifticonQuery.ListGifticons result = mapper.toGifticonListQuery(request);

    // then
    assertEquals("ACTIVE", result.getStatus());
    assertEquals(BigDecimal.valueOf(1000), result.getMinPrice());
    assertEquals(BigDecimal.valueOf(5000), result.getMaxPrice());
    assertEquals(List.of(1L), result.getCategory());
    assertEquals(2L, result.getSeller());
    assertEquals(3L, result.getBrand());
    assertEquals("아메리카노", result.getSearch());
    assertEquals(LocalDate.of(2024, 1, 1), result.getCreatedFrom());
    assertEquals(LocalDate.of(2024, 12, 31), result.getCreatedTo());

    assertNotNull(result.getPagination());
    assertEquals(2, result.getPagination().getPage());
    assertEquals(20, result.getPagination().getSize());
    assertEquals("price:asc", result.getPagination().getSort());
  }

  @Test
  void toGetGifticonDetailQuery_shouldMapCorrectly() {
    // given
    Long gifticonId = 123L;

    // when
    GifticonQuery.GetGifticon result = mapper.toGetGifticonDetailQuery(gifticonId);

    // then
    assertEquals(123L, result.getGifticonId());
  }
}