package com.mkhwang.gifticon.query.gifticon.application;

import com.mkhwang.gifticon.query.gifticon.domain.GifticonSearchDocument;
import com.mkhwang.gifticon.query.gifticon.presentation.dto.GifticonQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GifticonSearchOperationsImpl implements GifticonSearchOperations {
  private final ElasticsearchOperations elasticsearchOperations;

  @Override
  public SearchHits<GifticonSearchDocument> search(GifticonQuery.ListGifticons query) {
    Pageable pageable = query.getPagination().toPageable();

    return elasticsearchOperations.search((Query) null, GifticonSearchDocument.class);
  }
}
