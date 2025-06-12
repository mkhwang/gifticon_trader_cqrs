package com.mkhwang.trader.sync.application.handler.search;

import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkhwang.trader.common.brand.domain.Brand;
import com.mkhwang.trader.common.brand.infra.BrandRepository;
import com.mkhwang.trader.common.user.domain.User;
import com.mkhwang.trader.common.user.infra.UserRepository;
import com.mkhwang.trader.query.gifticon.domain.GifticonDocument;
import com.mkhwang.trader.query.gifticon.infra.GifticonDocumentRepository;
import com.mkhwang.trader.sync.application.handler.dto.CdcEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("GifticonPriceSearchModelEventHandler 단위 테스트")
@ExtendWith(MockitoExtension.class)
class GifticonPriceSearchModelEventHandlerTest {

  @Mock
  ElasticsearchOperations elasticsearchOperations;

  GifticonPriceSearchModelEventHandler handler;

  @BeforeEach
  void setUp() {
    handler = new GifticonPriceSearchModelEventHandler(new ObjectMapper(), elasticsearchOperations);
  }

  @Test
  @DisplayName("삭제 이벤트 처리 - basePrice, salePrice null로 업데이트")
  void shouldHandleDeleteEvent() {
    // given
    Long gifticonId = 1L;

    Map<String, Object> before = Map.of("gifticon_id", gifticonId);
    CdcEvent event = new CdcEvent();
    event.setOp("d");
    event.setBefore(before);

    // when
    handler.handle(event);

    // then
    ArgumentCaptor<UpdateQuery> captor = ArgumentCaptor.forClass(UpdateQuery.class);
    verify(elasticsearchOperations).update(captor.capture(), eq(IndexCoordinates.of("gifticons")));

    UpdateQuery query = captor.getValue();
    Document doc = query.getDocument();
    assertEquals(null, doc.get("basePrice"));
    assertEquals(null, doc.get("salePrice"));
  }

  @Test
  @DisplayName("base_price, sale_price가 있는 경우 업데이트 실행")
  void shouldUpdatePricesOnUpdateEvent() {
    Long gifticonId = 1L;

    Map<String, Object> after = Map.of(
            "gifticon_id", gifticonId,
            "base_price", 10000,
            "sale_price", 8000
    );

    CdcEvent event = new CdcEvent();
    event.setOp("u");
    event.setAfter(after);

    // when
    handler.handle(event);

    // then
    ArgumentCaptor<UpdateQuery> captor = ArgumentCaptor.forClass(UpdateQuery.class);
    verify(elasticsearchOperations).update(captor.capture(), eq(IndexCoordinates.of("gifticons")));

    Document doc = captor.getValue().getDocument();
    assertEquals(new BigDecimal("10000"), doc.get("basePrice"));
    assertEquals(new BigDecimal("8000"), doc.get("salePrice"));
  }

  @Test
  @DisplayName("gifticon_id가 없으면 아무 작업 안 함")
  void shouldSkipWhenGifticonIdMissing() {
    Map<String, Object> after = Map.of("base_price", "10000");
    CdcEvent event = new CdcEvent();
    event.setOp("u");
    event.setAfter(after);

    handler.handle(event);

    verifyNoInteractions(elasticsearchOperations);
  }

  @Test
  @DisplayName("updatePartialDocument에서 예외 발생 시 로그 출력")
  void shouldLogErrorWhenUpdateFails() {
    Long gifticonId = 1L;
    Map<String, Object> after = Map.of(
            "gifticon_id", gifticonId,
            "base_price", "10000"
    );
    CdcEvent event = new CdcEvent();
    event.setOp("u");
    event.setAfter(after);

    doThrow(new RuntimeException("Elasticsearch failure"))
            .when(elasticsearchOperations)
            .update(any(UpdateQuery.class), any(IndexCoordinates.class));

    handler.handle(event);

    // 예외는 throw되지 않고 내부 처리됨
    verify(elasticsearchOperations).update(any(), any());
  }
}