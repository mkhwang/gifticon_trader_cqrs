package com.mkhwang.trader.query.gifticon.application.search;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import com.mkhwang.trader.query.gifticon.application.query.GifticonQuery;
import org.springframework.stereotype.Component;

@Component
public class CreatedRangeSpec implements SearchQuerySpec {
  @Override
  public Query build(GifticonQuery.ListGifticons query) {
    if (query.getCreatedTo() == null || query.getCreatedFrom() == null) return null;

    return RangeQuery.of(rq ->
            rq.date(nrq ->
                    nrq.field("createdAt")
                            .lte(String.valueOf(query.getCreatedTo()))
                            .gte(String.valueOf(query.getCreatedFrom()))
            )
    )._toQuery();
  }
}
