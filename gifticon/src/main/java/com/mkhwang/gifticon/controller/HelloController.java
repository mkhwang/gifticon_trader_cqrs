package com.mkhwang.gifticon.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HelloController {

  @GetMapping("/api/hello")
  public ApiResponse<Object> hello() {
    return ApiResponse.success(Map.of("hello", "world"), "Hello, World! 요청이 성공적으로 처리되었습니다.");
  }
}
