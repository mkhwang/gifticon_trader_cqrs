package com.mkhwang.trader.query.gifticon.presentation;

import com.mkhwang.trader.common.dto.ApiResponse;
import com.mkhwang.trader.query.gifticon.application.GifticonQueryHandler;
import com.mkhwang.trader.query.gifticon.application.dto.GifticonQueryDto;
import com.mkhwang.trader.query.gifticon.application.query.GifticonQuery;
import com.mkhwang.trader.query.gifticon.presentation.dto.GifticonListRequest;
import com.mkhwang.trader.query.gifticon.presentation.dto.GifticonListResponse;
import com.mkhwang.trader.query.gifticon.presentation.mapper.GiftionQueryMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GifticonQueryController {
  private final GifticonQueryHandler gifticonQueryHandler;
  private final GiftionQueryMapper giftionQueryMapper;

  @Operation(summary = "Gifticons 상세 조회", description = "Gifticons 상세 조회 API")
  @GetMapping("/api/gifticon/{id}")
  public ResponseEntity<ApiResponse<GifticonQueryDto.Gifticon>> getGifticon(@PathVariable Long id) {
    GifticonQuery.GetGifticon query = giftionQueryMapper.toGetGifticonDetailQuery(id);
    GifticonQueryDto.Gifticon foundGifticon = gifticonQueryHandler.getGifticon(query);
    return ResponseEntity.ok(ApiResponse.success(foundGifticon, "기프티콘 상세 정보를 성공적으로 조회했습니다."));
  }

  @Operation(summary = "Gifticons 검색", description = "Gifticons 감섹 API")
  @GetMapping("/api/gifticon")
  public ResponseEntity<ApiResponse<GifticonListResponse>> getGifticons(@Valid @ParameterObject GifticonListRequest request) {
    GifticonQuery.ListGifticons query = giftionQueryMapper.toGifticonListQuery(request);
    return ResponseEntity.ok(ApiResponse.success(
            gifticonQueryHandler.getGifticons(query),
            "기프티콘 목록을 성공적으로 조회했습니다."
    ));
  }
}
