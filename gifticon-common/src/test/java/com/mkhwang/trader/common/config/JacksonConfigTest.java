package com.mkhwang.trader.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JacksonConfigTest {


  @Test
  @DisplayName("LocalDate 직렬화 테스트")
  void shouldSerializeLocalDateAsIso() throws Exception {
    ObjectMapper mapper = new JacksonConfig().objectMapper();
    LocalDate date = LocalDate.of(2025, 6, 12);

    String json = mapper.writeValueAsString(date);

    assertEquals("\"2025-06-12\"", json);
  }
}