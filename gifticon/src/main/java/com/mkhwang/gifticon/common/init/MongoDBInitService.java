package com.mkhwang.gifticon.common.init;

import com.mkhwang.gifticon.query.gifticon.domain.GifticonDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MongoDBInitService {

  private final MongoTemplate mongoTemplate;

  @EventListener(ApplicationReadyEvent.class)
  public void initCollections() {
    try {
      log.info("Initializing MongoDB collections...");

      if (!mongoTemplate.collectionExists(GifticonDocument.class)) {
        Collation collation = Collation.of("ko") // or "en", "simple"
                .strength(Collation.ComparisonLevel.secondary());
        CollectionOptions options = CollectionOptions.empty().collation(collation);

        mongoTemplate.createCollection(GifticonDocument.class, options);
        log.info("Created gifticon collection");
      }

      log.info("MongoDB collections initialized successfully");
    } catch (Exception e) {
      log.error("Failed to initialize MongoDB collections", e);
    }
  }

}