package com.mkhwang.gifticon.query.gifticon.application;

import com.mkhwang.gifticon.query.gifticon.domain.GifticonSearchDocument;
import com.mkhwang.gifticon.query.gifticon.presentation.dto.GifticonQuery;
import org.springframework.data.elasticsearch.core.SearchHits;

public interface GifticonSearchOperations {

  SearchHits<GifticonSearchDocument> search(GifticonQuery.ListGifticons query);
}
