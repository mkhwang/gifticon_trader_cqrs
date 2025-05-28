package com.mkhwang.gifticon.query.gifticon.application.search;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.mkhwang.gifticon.query.gifticon.presentation.dto.GifticonQuery;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class StatusSpec implements SearchQuerySpec {
  @Override
  public Query build(GifticonQuery.ListGifticons query) {
    if (!StringUtils.hasText(query.getStatus())) return null;

    return MatchQuery.of(m -> m
            .field("status")
            .query(query.getStatus())
    )._toQuery();
  }
}
