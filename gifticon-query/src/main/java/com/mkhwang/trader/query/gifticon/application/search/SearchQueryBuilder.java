package com.mkhwang.trader.query.gifticon.application.search;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.mkhwang.trader.query.gifticon.application.query.GifticonQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SearchQueryBuilder {

  private final List<SearchQuerySpec> specs;

  public Query build(GifticonQuery.ListGifticons query) {
    return BoolQuery.of(b -> b.must(specs.stream().map(spec -> spec.build(query))
            .filter(Objects::nonNull).toList()))._toQuery();
  }

}
