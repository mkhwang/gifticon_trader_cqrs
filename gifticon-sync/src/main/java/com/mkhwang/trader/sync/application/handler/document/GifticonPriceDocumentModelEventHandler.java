package com.mkhwang.trader.sync.application.handler.document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkhwang.trader.query.gifticon.domain.GifticonDocument;
import com.mkhwang.trader.query.gifticon.infra.GifticonDocumentRepository;
import com.mkhwang.trader.sync.application.handler.dto.CdcEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class GifticonPriceDocumentModelEventHandler extends GifticonDocumentModelBaseEventHandler {

  private final GifticonDocumentRepository gifticonDocumentRepository;

  public GifticonPriceDocumentModelEventHandler(ObjectMapper objectMapper, GifticonDocumentRepository gifticonDocumentRepository) {
    super(objectMapper);
    this.gifticonDocumentRepository = gifticonDocumentRepository;
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

    if (data == null || !data.containsKey("gifticon_id")) {
      return;
    }

    gifticonId = getLongValue(data, "gifticon_id");
    Optional<GifticonDocument> optionalDocument = gifticonDocumentRepository.findById(gifticonId);

    if (optionalDocument.isEmpty()) {
      log.warn("gifticon document not found for price update: {}", gifticonId);
      return;
    }

    GifticonDocument document = optionalDocument.get();

    // 삭제 이벤트 처리
    if (event.isDelete()) {
      document.setPrice(null);
      gifticonDocumentRepository.save(document);
      return;
    }

    // 가격 정보 업데이트
    Map<String, Object> priceInfo = document.getPrice();
    if (priceInfo == null) {
      priceInfo = new HashMap<>();
      document.setPrice(priceInfo);
    }

    if (data.containsKey("base_price")) {
      priceInfo.put("basePrice", getBigDecimalValue(data, "base_price"));
    }

    if (data.containsKey("sale_price")) {
      priceInfo.put("salePrice", getBigDecimalValue(data, "sale_price"));
    }


    if (data.containsKey("currency")) {
      priceInfo.put("currency", getBigDecimalValue(data, "currency"));
    }

    if (priceInfo.get("basePrice") != null && priceInfo.get("basePrice") instanceof BigDecimal basePrice
            && priceInfo.get("salePrice") != null && priceInfo.get("salePrice") instanceof BigDecimal salePrice
    ) {
      if (basePrice.compareTo(salePrice) > 0) {
        BigDecimal discount = basePrice.subtract(salePrice)
                .divide(basePrice, 10, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP); // 소수점 2자리 반올림

        priceInfo.put("discountPercentage", discount);
      } else {
        priceInfo.put("discountPercentage", BigDecimal.ZERO);
      }
    }


    gifticonDocumentRepository.save(document);
    log.info("Updated gifticon price information for gifticon ID: {}", gifticonId);
  }
}