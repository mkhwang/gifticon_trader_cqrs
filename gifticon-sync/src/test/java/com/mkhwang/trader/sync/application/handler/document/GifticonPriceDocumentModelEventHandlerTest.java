package com.mkhwang.trader.sync.application.handler.document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkhwang.trader.query.gifticon.domain.GifticonDocument;
import com.mkhwang.trader.query.gifticon.infra.GifticonDocumentRepository;
import com.mkhwang.trader.sync.application.handler.dto.CdcEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("GifticonPriceDocumentModelEventHandler 단위 테스트")
@ExtendWith(MockitoExtension.class)
class GifticonPriceDocumentModelEventHandlerTest {

  @Mock
  GifticonDocumentRepository gifticonDocumentRepository;

  GifticonPriceDocumentModelEventHandler handler;

  @BeforeEach
  void setUp() {
    handler = new GifticonPriceDocumentModelEventHandler(new ObjectMapper(), gifticonDocumentRepository);
  }

  @Test
  @DisplayName("삭제 이벤트 시 - price null 설정 및 저장")
  void shouldRemovePriceOnDelete() {
    // given
    Long gifticonId = 1L;
    Map<String, Object> before = Map.of("gifticon_id", gifticonId);
    CdcEvent event = new CdcEvent();
    event.setOp("d");
    event.setBefore(before);

    GifticonDocument doc = GifticonDocument.builder().id(gifticonId).price(new HashMap<>()).build();
    given(gifticonDocumentRepository.findById(gifticonId)).willReturn(Optional.of(doc));

    // when
    handler.handle(event);

    // then
    assertNull(doc.getPrice());
    verify(gifticonDocumentRepository).save(doc);
  }

  @Test
  @DisplayName("가격 정보 업데이트 시 - basePrice, salePrice, discount 계산 저장")
  void shouldUpdatePriceFieldsAndCalculateDiscount() {
    // given
    Long gifticonId = 1L;
    Map<String, Object> after = Map.of(
            "gifticon_id", gifticonId,
            "base_price", 10000,
            "sale_price", 8000,
            "currency", "KRW"
    );

    CdcEvent event = new CdcEvent();
    event.setOp("u");
    event.setAfter(after);

    GifticonDocument doc = GifticonDocument.builder().id(gifticonId).build();
    given(gifticonDocumentRepository.findById(gifticonId)).willReturn(Optional.of(doc));

    // when
    handler.handle(event);

    // then
    Map<String, Object> price = doc.getPrice();
    assertNotNull(price);
    assertEquals(new BigDecimal("10000"), price.get("basePrice"));
    assertEquals(new BigDecimal("8000"), price.get("salePrice"));

    BigDecimal discount = (BigDecimal) price.get("discountPercentage");
    assertEquals(new BigDecimal("20.00"), discount); // 20% 할인

    verify(gifticonDocumentRepository).save(doc);
  }

  @Test
  @DisplayName("할인율 0% 처리 - salePrice >= basePrice")
  void shouldSetDiscountZeroWhenNoDiscount() {
    // given
    Long gifticonId = 1L;
    Map<String, Object> after = Map.of(
            "gifticon_id", gifticonId,
            "base_price", "10000",
            "sale_price", "10000"
    );

    CdcEvent event = new CdcEvent();
    event.setOp("u");
    event.setAfter(after);

    GifticonDocument doc = GifticonDocument.builder().id(gifticonId).build();
    given(gifticonDocumentRepository.findById(gifticonId)).willReturn(Optional.of(doc));

    // when
    handler.handle(event);

    // then
    assertEquals(BigDecimal.ZERO, doc.getPrice().get("discountPercentage"));
    verify(gifticonDocumentRepository).save(doc);
  }

  @Test
  @DisplayName("gifticon_id 없으면 무시")
  void shouldSkipWhenGifticonIdMissing() {
    // given
    Map<String, Object> after = Map.of("base_price", "10000");
    CdcEvent event = new CdcEvent();
    event.setOp("c");
    event.setAfter(after);

    // when
    handler.handle(event);

    // then
    verifyNoInteractions(gifticonDocumentRepository);
  }

  @Test
  @DisplayName("문서 없으면 무시")
  void shouldSkipWhenDocumentNotFound() {
    // when
    Map<String, Object> after = Map.of("gifticon_id", 999L, "base_price", "10000");
    CdcEvent event = new CdcEvent();
    event.setOp("c");
    event.setAfter(after);

    given(gifticonDocumentRepository.findById(999L)).willReturn(Optional.empty());

    // given
    handler.handle(event);

    // then
    verify(gifticonDocumentRepository, never()).save(any());
  }
}