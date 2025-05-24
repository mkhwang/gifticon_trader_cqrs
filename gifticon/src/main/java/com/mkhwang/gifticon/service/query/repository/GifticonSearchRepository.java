package com.mkhwang.gifticon.service.query.repository;

import com.mkhwang.gifticon.service.query.entity.GifticonSearchDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface GifticonSearchRepository extends ElasticsearchRepository<GifticonSearchDocument, Long> {
}
