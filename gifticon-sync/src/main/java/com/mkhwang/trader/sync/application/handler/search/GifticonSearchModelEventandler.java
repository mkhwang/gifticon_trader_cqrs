package com.mkhwang.trader.sync.application.handler.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkhwang.trader.common.brand.infra.BrandRepository;
import com.mkhwang.trader.common.category.infra.CategoryRepository;
import com.mkhwang.trader.common.user.infra.UserRepository;
import com.mkhwang.trader.query.gifticon.domain.GifticonSearchDocument;
import com.mkhwang.trader.query.gifticon.infra.GifticonSearchRepository;
import com.mkhwang.trader.sync.application.handler.dto.CdcEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class GifticonSearchModelEventandler extends GifticonSearchModelBaseEventHandler {

  private final GifticonSearchRepository gifticonSearchRepository;
  private final UserRepository userRepository;
  private final BrandRepository brandRepository;
  private final CategoryRepository categoryRepository;

  public GifticonSearchModelEventandler(ObjectMapper objectMapper, GifticonSearchRepository gifticonSearchRepository,
                                        UserRepository userRepository, BrandRepository brandRepository,
                                        CategoryRepository categoryRepository) {
    super(objectMapper);
    this.gifticonSearchRepository = gifticonSearchRepository;
    this.userRepository = userRepository;
    this.brandRepository = brandRepository;
    this.categoryRepository = categoryRepository;
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

    userRepository.findById(getLongValue(data, "seller_id"))
        .ifPresent(user -> {
          document.setSeller(user.getNickname());
          document.setSellerId(user.getId());
        });
    brandRepository.findById(getLongValue(data, "brand_id")).ifPresent(brand -> {
      document.setBrand(brand.getName());
    });
    categoryRepository.findById(getLongValue(data, "category_id")).ifPresent(category -> {
      document.setCategory(category.getName());
    });

    if (data.containsKey("buyer_id") && getLongValue(data, "buyer_id") != null) {
      userRepository.findById(getLongValue(data, "buyer_id"))
          .ifPresent(user -> document.setBuyer(user.getNickname()));
    }

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
      document.setTags(existing.getTags());
    }

    // 저장
    gifticonSearchRepository.save(document);
    log.info("Saved gifticon document: {}", gifticonId);
  }
}