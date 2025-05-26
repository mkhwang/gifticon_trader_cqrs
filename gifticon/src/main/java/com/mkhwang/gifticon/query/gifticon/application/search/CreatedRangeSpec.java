package com.mkhwang.gifticon.query.gifticon.application.search;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import com.mkhwang.gifticon.query.gifticon.presentation.dto.GifticonQuery;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

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
