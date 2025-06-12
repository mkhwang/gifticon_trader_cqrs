package com.mkhwang.trader.sync.application.handler.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkhwang.trader.sync.application.handler.dto.CdcEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("GifticonSearchModelBaseEventHandler parseTimestampToInstant() 테스트")
class GifticonSearchModelBaseEventHandlerTest {

  private GifticonSearchModelBaseEventHandler handler;

  @BeforeEach
  void setUp() {
    handler = new GifticonSearchModelBaseEventHandler(new ObjectMapper()) {
      @Override
      protected String getSupportedTable() {
        return "gifticons";
      }

      @Override
      public void handle(CdcEvent event) {
        // 테스트용 dummy 구현
      }
    };
  }

  @Test
  @DisplayName("ISO 포맷 문자열을 Instant로 파싱")
  void testParseIsoStringTimestamp() {
    // given
    String timestampStr = "2025-06-12T12:00:00";

    // when
    Instant result = handler.parseTimestampToInstant(timestampStr);

    LocalDateTime local = LocalDateTime.parse(timestampStr);
    Instant expected = local.atZone(ZoneId.systemDefault()).toInstant();

    // then
    assertEquals(expected, result);
  }

  @Test
  @DisplayName("마이크로초 단위 숫자를 Instant로 변환")
  void testParseNumericTimestamp() {
    // given
    long micro = 1_725_000_000_000_000L;

    // when
    Instant result = handler.parseTimestampToInstant(micro);

    // then
    Instant expected = Instant.ofEpochMilli(micro / 1000);
    assertEquals(expected, result);
  }

  @Test
  @DisplayName("잘못된 문자열은 null 반환")
  void testParseInvalidStringTimestamp() {
    // given
    String badInput = "invalid-timestamp";

    // when
    Instant result = handler.parseTimestampToInstant(badInput);

    // then
    assertNull(result);
  }

  @Test
  @DisplayName("null 입력은 null 반환")
  void testParseNullTimestamp() {
    assertNull(handler.parseTimestampToInstant(null));
  }

  @Test
  @DisplayName("지원하지 않는 타입은 null 반환")
  void testParseUnsupportedType() {
    assertNull(handler.parseTimestampToInstant(new Object()));
  }
}