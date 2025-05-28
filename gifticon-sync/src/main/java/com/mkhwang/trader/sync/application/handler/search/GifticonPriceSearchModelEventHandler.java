package com.mkhwang.trader.sync.application.handler.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkhwang.trader.sync.application.handler.dto.CdcEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class GifticonPriceSearchModelEventHandler extends GifticonSearchModelBaseEventHandler {

  private final ElasticsearchOperations elasticsearchOperations;

  public GifticonPriceSearchModelEventHandler(
          ObjectMapper objectMapper,
          ElasticsearchOperations elasticsearchOperations) {
    super(objectMapper);
    this.elasticsearchOperations = elasticsearchOperations;
  }

  @Override
  protected String getSupportedTable() {
    return "gifticon_prices";
  }

  @Override
  public void handle(CdcEvent event) {
    Map<String, Object> data;
    Long gifticonId;

    if (event.isDelete()) {
      data = event.getBeforeData();
    } else {
      data = event.getAfterData();
    }

    if (data == null || !data.containsKey("gifticoin_id")) {
      return;
    }

    gifticonId = getLongValue(data, "gifticoin_id");

    // 삭제 이벤트 처리
    if (event.isDelete()) {
      Map<String, Object> updates = new HashMap<>();
      updates.put("basePrice", null);
      updates.put("salePrice", null);
      updatePartialDocument(gifticonId, updates);
      return;
    }

    // 부분 업데이트로 처리
    Map<String, Object> updates = new HashMap<>();

    if (data.containsKey("base_price")) {
      updates.put("basePrice", getBigDecimalValue(data, "base_price"));
    }

    if (data.containsKey("sale_price")) {
      updates.put("salePrice", getBigDecimalValue(data, "sale_price"));
    }

    if (!updates.isEmpty()) {
      updatePartialDocument(gifticonId, updates);
    }
  }

  // 부분 업데이트를 위한 메서드
  private void updatePartialDocument(Long gifticonId, Map<String, Object> updates) {
    if (gifticonId == null || updates == null || updates.isEmpty()) {
      return;
    }

    try {
      // Document 객체 생성
      Document document = Document.create();

      // 각 필드를 Document에 추가
      updates.forEach(document::put);

      UpdateQuery updateQuery = UpdateQuery.builder(gifticonId.toString())
              .withDocument(document)
              .withDocAsUpsert(true) // 문서가 없으면 생성
              .build();

      elasticsearchOperations.update(updateQuery, IndexCoordinates.of("gifticons"));
      log.debug("Partially updated gifticon price: {}", gifticonId);
    } catch (Exception e) {
      log.error("Error updating gifticon price {}: {}", gifticonId, e.getMessage());
    }
  }
}