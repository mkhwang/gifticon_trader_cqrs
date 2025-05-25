package com.mkhwang.gifticon.common.init;

import com.mkhwang.gifticon.query.gifticon.domain.GifticonSearchDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElasticsearchInitService {

  private final ElasticsearchOperations elasticsearchOperations;

  @EventListener(ApplicationReadyEvent.class)
  public void initIndices() {
    try {
      log.info("Initializing Elasticsearch indices...");

      IndexOperations indexOps = elasticsearchOperations.indexOps(GifticonSearchDocument.class);

      if (!indexOps.exists()) {
        log.info("Creating gifticon index with Nori analyzer");
        indexOps.create();
        indexOps.putMapping(indexOps.createMapping());
      }
      log.info("Elasticsearch indices initialized successfully");
    } catch (Exception e) {
      log.error("Failed to initialize Elasticsearch indices", e);
    }
  }
}