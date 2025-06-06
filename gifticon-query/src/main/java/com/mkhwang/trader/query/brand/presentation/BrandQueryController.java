package com.mkhwang.trader.query.brand.presentation;

import com.mkhwang.trader.common.dto.ApiResponse;
import com.mkhwang.trader.query.brand.application.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Brand", description = "Brand API")
@RestController
@RequiredArgsConstructor
public class BrandQueryController {
  private final BrandService brandService;

  @Operation(summary = "Brand 조회", description = "Brand 목록 조회 API")
  @GetMapping("/api/brands")
  public ResponseEntity<?> getAllBrands() {
    return ResponseEntity.ok(ApiResponse.success(brandService.getAllBrands(), "브랜드 목록을 성공적으로 조회했습니다."));
  }
}
