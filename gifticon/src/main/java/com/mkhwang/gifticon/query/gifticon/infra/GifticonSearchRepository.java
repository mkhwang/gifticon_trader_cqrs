package com.mkhwang.gifticon.query.gifticon.infra;

import com.mkhwang.gifticon.query.gifticon.domain.GifticonSearchDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GifticonSearchRepository extends ElasticsearchRepository<GifticonSearchDocument, Long> {
}
