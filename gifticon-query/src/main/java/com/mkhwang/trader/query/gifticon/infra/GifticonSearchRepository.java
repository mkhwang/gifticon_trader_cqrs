package com.mkhwang.trader.query.gifticon.infra;

import com.mkhwang.trader.query.gifticon.domain.GifticonSearchDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GifticonSearchRepository extends ElasticsearchRepository<GifticonSearchDocument, Long> {
}
