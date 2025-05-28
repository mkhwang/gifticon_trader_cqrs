package com.mkhwang.gifticon.query.gifticon.application.search;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.mkhwang.gifticon.query.gifticon.presentation.dto.GifticonQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class KeywordSpecTest {
  private final KeywordSpec keywordSpec = new KeywordSpec();

  @DisplayName("Keyword 조건 검색 테스트")
  @Test
  void keywordSpecTest() {
    // given
    String keyword = "test";
    GifticonQuery.ListGifticons query = GifticonQuery.ListGifticons.builder()
            .search(keyword)
            .build();

    // when
    Query result = keywordSpec.build(query);

    // then
    assertNotNull(result.bool());
    BoolQuery boolQuery = result.bool();

    assertEquals(boolQuery.should().size(), 6);
    assertEquals(boolQuery.minimumShouldMatch(), "1");

    List<String> expectedFields = List.of("name", "description", "category", "seller", "brand", "tags");

    List<String> actualFields = boolQuery.should().stream()
            .map(q -> switch (q._kind()) {
              case Match -> q.match().field();
              case Terms -> q.terms().field();
              default -> null;
            })
            .toList();

    assertThat(actualFields).containsExactlyElementsOf(expectedFields);
  }

  @DisplayName("Keyword 조건 검색이 없을 때 null 반환 테스트")
  @Test
  void keywordSpecNullTest() {
    // given
    GifticonQuery.ListGifticons query = GifticonQuery.ListGifticons.builder()
            .search(null)
            .build();

    // when
    Query result = keywordSpec.build(query);

    // then
    assertNull(result);
  }
}