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
