package com.mkhwang.gifticon.query.gifticon.application.search;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import com.mkhwang.gifticon.query.gifticon.presentation.dto.GifticonQuery;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@Component
public class MaxPriceSpeck implements SearchQuerySpec {
  @Override
  public Query build(GifticonQuery.ListGifticons query) {
    if (query.getMaxPrice() == null) return null;

    return RangeQuery.of(rq ->
            rq.number(nrq ->
                    nrq.field("salePrice")
                            .lte(query.getMaxPrice().doubleValue())
            )
    )._toQuery();
  }
}
