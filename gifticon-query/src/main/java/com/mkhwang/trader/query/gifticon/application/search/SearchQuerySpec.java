package com.mkhwang.trader.query.gifticon.application.search;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.mkhwang.trader.query.gifticon.application.query.GifticonQuery;
import org.springframework.lang.Nullable;

public interface SearchQuerySpec {
  @Nullable
  Query build(GifticonQuery.ListGifticons query);
}
