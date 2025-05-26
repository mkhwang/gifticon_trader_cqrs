package com.mkhwang.gifticon.query.gifticon.application.search;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermsQuery;
import com.mkhwang.gifticon.query.gifticon.presentation.dto.GifticonQuery;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import java.util.List;

@Component
public class KeywordSpec implements SearchQuerySpec {
  @Override
  public Query build(GifticonQuery.ListGifticons query) {
    if (!StringUtils.hasText(query.getSearch())) return null;

    List<Query> orConditions = List.of(
            MatchQuery.of(m -> m.field("name").query(query.getSearch()))._toQuery(),
            MatchQuery.of(m -> m.field("description").query(query.getSearch()))._toQuery(),
            MatchQuery.of(m -> m.field("category").query(query.getSearch()))._toQuery(),
            MatchQuery.of(m -> m.field("seller").query(query.getSearch()))._toQuery(),
            MatchQuery.of(m -> m.field("brand").query(query.getSearch()))._toQuery(),
            TermsQuery.of(t -> t
                    .field("tags")
                    .terms(ts -> ts
                            .value(List.of(FieldValue.of(query.getSearch())))
                    )
            )._toQuery()
    );

    return BoolQuery.of(b -> b
            .should(orConditions)
            .minimumShouldMatch("1")
    )._toQuery();
  }
}
