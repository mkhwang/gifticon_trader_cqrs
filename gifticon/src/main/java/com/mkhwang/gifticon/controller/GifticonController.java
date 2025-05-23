package com.mkhwang.gifticon.controller;

import com.mkhwang.gifticon.controller.dto.GifticonListRequest;
import com.mkhwang.gifticon.controller.dto.GifticonListResponse;
import com.mkhwang.gifticon.controller.mapper.GifticonMapper;
import com.mkhwang.gifticon.service.gifticon.GifticonDto;
import com.mkhwang.gifticon.service.query.GifticonQuery;
import com.mkhwang.gifticon.service.query.GifticonQueryHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Gifticons", description = "Gifticons API")
@RestController
@RequiredArgsConstructor
public class GifticonController {
  private final GifticonQueryHandler gifticonQueryHandler;
  private final GifticonMapper mapper;

  @Operation(summary = "Gifticons 상세 조회", description = "Gifticons 상세 조회 API")
  @GetMapping("/api/gifticon/{id}")
  public ResponseEntity<ApiResponse<GifticonDto.Gifticon>> getProduct(@PathVariable Long id) {
    GifticonQuery.GetGifticon query = GifticonQuery.GetGifticon.builder()
        .gifticonId(id)
        .build();
    GifticonDto.Gifticon foundProduct = gifticonQueryHandler.getGifticon(query);
    return ResponseEntity.ok(ApiResponse.success(foundProduct, "상품 상세 정보를 성공적으로 조회했습니다."));
  }

  @Operation(summary = "Gifticons 검색", description = "Gifticons 감섹 API")
  @GetMapping("/api/gifticon")
  public ResponseEntity<ApiResponse<GifticonListResponse>> getProducts(@Valid @ParameterObject GifticonListRequest request) {
    GifticonQuery.ListGifticons query = mapper.toProductDtoListRequest(request);
    return ResponseEntity.ok(ApiResponse.success(
            gifticonQueryHandler.getGifticons(query),
            "상품 목록을 성공적으로 조회했습니다."
    ));
  }
}
