package com.mkhwang.trader.query.gifticon.application;

import com.mkhwang.trader.query.gifticon.application.query.GifticonQuery;
import com.mkhwang.trader.query.gifticon.domain.GifticonSearchDocument;
import org.springframework.data.elasticsearch.core.SearchHits;

public interface GifticonSearchOperations {

  SearchHits<GifticonSearchDocument> search(GifticonQuery.ListGifticons query);
}
