package com.mkhwang.trader.sync.application.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkhwang.trader.sync.application.handler.dto.CdcEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AbstractCdcEventHandler 보조 메서드 테스트")
class AbstractCdcEventHandlerTest {

  private AbstractCdcEventHandler handler;

  @BeforeEach
  void setUp() {
    handler = new AbstractCdcEventHandler(new ObjectMapper()) {
      @Override
      protected String getSupportedTable() {
        return "gifticon";
      }

      @Override
      public void handle(CdcEvent event) {
        // 테스트용: do nothing
      }
    };
  }

  @Test
  @DisplayName("canHandle()은 table 이름이 일치하면 true 반환")
  void testCanHandle() {
    CdcEvent event = new CdcEvent();
    CdcEvent.Source source = new CdcEvent.Source();
    source.setTable("gifticon");
    event.setSource(source);

    assertTrue(handler.canHandle(event));
  }

  @Test
  @DisplayName("getStringValue()는 key가 존재하고 null이 아니면 문자열 반환")
  void testGetStringValue() {
    Map<String, Object> data = Map.of("name", "hello");
    assertEquals("hello", handler.getStringValue(data, "name"));
  }

  @Test
  @DisplayName("getLongValue()는 숫자나 문자열을 Long으로 변환")
  void testGetLongValue() {
    Map<String, Object> map = new HashMap<>();
    map.put("num1", 123L);
    map.put("num2", "456");
    map.put("num3", "bad");

    assertEquals(123L, handler.getLongValue(map, "num1"));
    assertEquals(456L, handler.getLongValue(map, "num2"));
    assertNull(handler.getLongValue(map, "num3"));
  }

  @Test
  @DisplayName("getIntegerValue()는 숫자나 문자열을 Integer로 변환")
  void testGetIntegerValue() {
    Map<String, Object> map = new HashMap<>();
    map.put("a", 10);
    map.put("b", "20");
    map.put("c", "abc");

    assertEquals(10, handler.getIntegerValue(map, "a"));
    assertEquals(20, handler.getIntegerValue(map, "b"));
    assertNull(handler.getIntegerValue(map, "c"));
  }

  @Test
  @DisplayName("getDoubleValue()는 숫자나 문자열을 Double로 변환")
  void testGetDoubleValue() {
    Map<String, Object> map = new HashMap<>();
    map.put("a", 1.5);
    map.put("b", "2.7");
    map.put("c", "bad");

    assertEquals(1.5, handler.getDoubleValue(map, "a"));
    assertEquals(2.7, handler.getDoubleValue(map, "b"));
    assertNull(handler.getDoubleValue(map, "c"));
  }

  @Test
  @DisplayName("getBigDecimalValue()는 숫자, 문자열, base64 문자열 처리")
  void testGetBigDecimalValue() {
    Map<String, Object> map = new HashMap<>();
    map.put("a", "123.45");
    map.put("b", 678.90);
    map.put("c", Base64.getEncoder().encodeToString(new BigInteger("12345").toByteArray())); // scale 2 → 123.45

    BigDecimal valA = handler.getBigDecimalValue(map, "a");
    BigDecimal valB = handler.getBigDecimalValue(map, "b");
    BigDecimal valC = handler.getBigDecimalValue(map, "c");

    assertEquals(new BigDecimal("123.45"), valA);
    assertEquals(new BigDecimal("678.9"), valB);
    assertEquals(new BigDecimal("123.45"), valC);
  }

  @Test
  @DisplayName("getBooleanValue()는 다양한 truthy/falsey 값 인식")
  void testGetBooleanValue() {
    Map<String, Object> map = Map.of(
            "a", "true",
            "b", "1",
            "c", "yes",
            "d", "t",
            "e", "y",
            "f", false,
            "g", "no"
    );

    assertTrue(handler.getBooleanValue(map, "a"));
    assertTrue(handler.getBooleanValue(map, "b"));
    assertTrue(handler.getBooleanValue(map, "c"));
    assertTrue(handler.getBooleanValue(map, "d"));
    assertTrue(handler.getBooleanValue(map, "e"));
    assertFalse(handler.getBooleanValue(map, "f"));
    assertFalse(handler.getBooleanValue(map, "g"));
  }
}