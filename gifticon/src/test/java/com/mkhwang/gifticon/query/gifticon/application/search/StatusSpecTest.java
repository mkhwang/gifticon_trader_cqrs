package com.mkhwang.gifticon.query.gifticon.application.search;

import com.mkhwang.gifticon.query.gifticon.presentation.dto.GifticonQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusSpecTest {
  private final StatusSpec statusSpec = new StatusSpec();

  @DisplayName("상태 조건 검색 테스트")
  @Test
  void testStatusSpec() {
    // given
    String status = "ACTIVE";
    GifticonQuery.ListGifticons query = GifticonQuery.ListGifticons.builder()
            .status(status)
            .build();

    // when
    var result = statusSpec.build(query);

    // then
    assertNotNull(result);
    assertEquals("status", result.match().field());
    assertEquals(status, result.match().query().stringValue());
  }
}