package com.mkhwang.gifticon.sync.handler.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkhwang.gifticon.query.gifticon.domain.GifticonSearchDocument;
import com.mkhwang.gifticon.query.gifticon.infra.GifticonSearchRepository;
import com.mkhwang.gifticon.sync.handler.dto.CdcEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class GifticonSearchModelEventandler extends GifticonSearchModelBaseEventHandler {

  private final GifticonSearchRepository gifticonSearchRepository;

  public GifticonSearchModelEventandler(ObjectMapper objectMapper, GifticonSearchRepository gifticonSearchRepository) {
    super(objectMapper);
    this.gifticonSearchRepository = gifticonSearchRepository;
  }

  @Override
  protected String getSupportedTable() {
    return "gifticons";
  }

  @Override
  public void handle(CdcEvent event) {
    if (event.isDelete()) {
      handleDelete(event);
    } else {
      handleCreateOrUpdate(event);
    }
  }

  private void handleDelete(CdcEvent event) {
    Map<String, Object> data = event.getBeforeData();
    if (data == null || !data.containsKey("id")) {
      return;
    }

    Long gifticonId = getLongValue(data, "id");
    gifticonSearchRepository.deleteById(gifticonId);
    log.info("Deleted gifticon document: {}", gifticonId);
  }

  private void handleCreateOrUpdate(CdcEvent event) {
    Map<String, Object> data = event.getAfterData();
    if (data == null || !data.containsKey("id")) {
      return;
    }

    Long gifticonId = getLongValue(data, "id");

    // 전체 문서 생성/업데이트를 위해 새 문서 생성
    // gifticons 테이블 이벤트는 항상 전체 문서로 처리
    GifticonSearchDocument document = new GifticonSearchDocument();
    document.setId(gifticonId);
    document.setName(getStringValue(data, "name"));
    document.setSlug(getStringValue(data, "slug"));
    document.setDescription(getStringValue(data, "description"));
    document.setStatus(getStringValue(data, "status"));
    document.setSellerId(getLongValue(data, "seller_id"));
    document.setBrandId(getLongValue(data, "brand_id"));

    // 날짜 필드 처리
    if (data.containsKey("created_at")) {
      document.setCreatedAt(parseTimestampToInstant(data.get("created_at")));
    }

    if (data.containsKey("updated_at")) {
      document.setUpdatedAt(parseTimestampToInstant(data.get("updated_at")));
    }

    // 기존 문서가 있을 경우 누락된 필드 복원
    Optional<GifticonSearchDocument> existingDoc = gifticonSearchRepository.findById(gifticonId);
    if (existingDoc.isPresent()) {
      GifticonSearchDocument existing = existingDoc.get();
      // 기존 필드 복원
      document.setBasePrice(existing.getBasePrice());
      document.setSalePrice(existing.getSalePrice());
      document.setCategoryId(existing.getCategoryId());
      document.setTags(existing.getTags());
    }

    // 저장
    gifticonSearchRepository.save(document);
    log.info("Saved gifticon document: {}", gifticonId);
  }
}