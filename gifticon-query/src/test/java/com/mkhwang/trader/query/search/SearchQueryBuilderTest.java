package com.mkhwang.trader.query.search;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.mkhwang.trader.query.gifticon.application.query.GifticonQuery;
import com.mkhwang.trader.query.gifticon.application.search.CreatedRangeSpec;
import com.mkhwang.trader.query.gifticon.application.search.MaxPriceSpec;
import com.mkhwang.trader.query.gifticon.application.search.SearchQueryBuilder;
import com.mkhwang.trader.query.gifticon.application.search.StatusSpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SearchQueryBuilderTest {
  private SearchQueryBuilder searchQueryBuilder;

  @BeforeEach
  void setup() {
    CreatedRangeSpec createdRangeSpec = new CreatedRangeSpec();
    MaxPriceSpec maxPriceSpeckTest = new MaxPriceSpec();
    StatusSpec statusSpec = new StatusSpec();

    searchQueryBuilder = new SearchQueryBuilder(
            List.of(createdRangeSpec, maxPriceSpeckTest, statusSpec));
  }


  @DisplayName("SearchQueryBuilder가 여러 spec을 조합하여 Query를 생성한다")
  @Test
  void query_spec_builder() {
    // given
    LocalDate from = LocalDate.of(2025, 1, 1);
    LocalDate to = LocalDate.of(2025, 1, 31);
    String status = "ACTIVE";
    GifticonQuery.ListGifticons query = GifticonQuery.ListGifticons.builder()
            .createdFrom(from)
            .createdTo(to)
            .status(status)
            .build();

    // when
    Query result = searchQueryBuilder.build(query);

    // then
    assertThat(result.bool()).isNotNull();
    List<Query> mustQueries = result.bool().must();
    assertThat(mustQueries).hasSize(2);

    Optional<Query> matchQueryOptional = mustQueries.stream().filter(
            Query::isMatch
    ).findFirst();

    assertThat(matchQueryOptional).isPresent();
    assertThat(matchQueryOptional.get().match().field()).isEqualTo("status");

    Optional<Query> rangeQueryOptional = mustQueries.stream().filter(
      Query::isRange
    ).findFirst();

    assertThat(rangeQueryOptional).isPresent();
    assertThat(rangeQueryOptional.get().range().date().field()).isEqualTo("createdAt");
  }

  @Test
  void 모든_spec이_null을_반환하면_must는_비어있다() {
    // given
    GifticonQuery.ListGifticons query = GifticonQuery.ListGifticons.builder().build();

    // when
    Query result = searchQueryBuilder.build(query);

    // then
    assertThat(result.bool()).isNotNull();
    assertThat(result.bool().must()).isEmpty();
  }
}