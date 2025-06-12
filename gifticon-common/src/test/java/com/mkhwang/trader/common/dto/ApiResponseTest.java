package com.mkhwang.trader.common.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class ApiResponseTest {

  @Test
  @DisplayName("data와 message를 전달하면 성공 응답이 생성된다")
  void shouldCreateSuccessResponseWithDataAndMessage() {
    String data = "Hello";
    String message = "성공 메시지";

    ApiResponse<String> response = ApiResponse.success(data, message);

    assertTrue(response.isSuccess());
    assertEquals(data, response.getData());
    assertEquals(message, response.getMessage());
  }

  @Test
  @DisplayName("data만 전달하면 기본 메시지가 포함된 성공 응답이 생성된다")
  void shouldCreateSuccessResponseWithDataOnly() {
    Integer data = 123;

    ApiResponse<Integer> response = ApiResponse.success(data);

    assertTrue(response.isSuccess());
    assertEquals(data, response.getData());
    assertEquals("요청이 성공적으로 처리되었습니다.", response.getMessage());
  }

  @Test
  @DisplayName("message만 전달하면 data는 null인 성공 응답이 생성된다")
  void shouldCreateSuccessResponseWithMessageOnly() {
    String message = "단순 메시지";

    ApiResponse<Void> response = ApiResponse.success(message);

    assertTrue(response.isSuccess());
    assertNull(response.getData());
    assertEquals(message, response.getMessage());
  }
}