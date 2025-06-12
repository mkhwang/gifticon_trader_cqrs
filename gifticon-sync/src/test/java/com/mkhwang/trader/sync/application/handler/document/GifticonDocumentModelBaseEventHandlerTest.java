package com.mkhwang.trader.sync.application.handler.document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkhwang.trader.sync.application.handler.dto.CdcEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GifticonDocumentModelBaseEventHandler parseTimestampToLocalDateTime 테스트")
class GifticonDocumentModelBaseEventHandlerTest {

  private GifticonDocumentModelBaseEventHandler handler;

  @BeforeEach
  void setUp() {
    handler = new GifticonDocumentModelBaseEventHandler(new ObjectMapper()) {
      @Override
      protected String getSupportedTable() {
        return "gifticon";
      }

      @Override
      public void handle(CdcEvent event) {
        // 테스트용 구현
      }
    };
  }

  @Test
  @DisplayName("ISO-8601 문자열(LocalDateTime) 파싱")
  void testParseIsoStringTimestamp() {
    String input = "2025-06-12T10:15:30";
    LocalDateTime result = handler.parseTimestampToLocalDateTime(input);

    assertEquals(LocalDateTime.of(2025, 6, 12, 10, 15, 30), result);
  }

  @Test
  @DisplayName("숫자 마이크로초 타임스탬프 파싱")
  void testParseNumericTimestamp() {
    long epochMicro = 1_725_000_000_000_000L; // 2025-06-12T00:00:00+09:00 기준 마이크로초
    LocalDateTime result = handler.parseTimestampToLocalDateTime(epochMicro);

    // 시스템 기본 타임존 기준으로 변환됨
    LocalDateTime expected = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(epochMicro / 1000),
            ZoneId.systemDefault());

    assertEquals(expected, result);
  }

  @Test
  @DisplayName("ISO_DATE_TIME 포맷 문자열 파싱")
  void testParseIsoDateTimeFormatString() {
    String isoZoned = "2025-06-12T12:00:00+09:00"; // Zoned datetime
    LocalDateTime result = handler.parseTimestampToLocalDateTime(isoZoned);

    // parse는 기본적으로 Offset을 무시하고 LocalDateTime에 맞춤
    assertEquals(LocalDateTime.of(2025, 6, 12, 12, 0), result);
  }

  @Test
  @DisplayName("잘못된 문자열 → null 반환 및 경고 로그")
  void testInvalidStringTimestamp() {
    String badInput = "not-a-date";
    LocalDateTime result = handler.parseTimestampToLocalDateTime(badInput);

    assertNull(result);
  }

  @Test
  @DisplayName("null 입력 → null 반환")
  void testNullTimestamp() {
    assertNull(handler.parseTimestampToLocalDateTime(null));
  }

  @Test
  @DisplayName("지원하지 않는 타입 입력 → null 반환")
  void testUnsupportedTypeTimestamp() {
    assertNull(handler.parseTimestampToLocalDateTime(new Object()));
  }
}