package com.mkhwang.gifticon.controller;

import com.mkhwang.gifticon.controller.dto.GifticonCreateRequest;
import com.mkhwang.gifticon.controller.dto.GifticonListRequest;
import com.mkhwang.gifticon.controller.dto.GifticonListResponse;
import com.mkhwang.gifticon.controller.dto.GifticonTradeRequest;
import com.mkhwang.gifticon.controller.mapper.GifticonMapper;
import com.mkhwang.gifticon.service.gifticon.GifticonCommand;
import com.mkhwang.gifticon.service.gifticon.GifticonCommandHandler;
import com.mkhwang.gifticon.service.gifticon.GifticonDto;
import com.mkhwang.gifticon.service.query.GifticonQuery;
import com.mkhwang.gifticon.service.query.GifticonQueryHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Gifticons", description = "Gifticons API")
@RestController
@RequiredArgsConstructor
public class GifticonController {
  private final GifticonQueryHandler gifticonQueryHandler;
  private final GifticonCommandHandler gifticonCommandHandler;
  private final GifticonMapper mapper;

  @Operation(summary = "Gifticons 상세 조회", description = "Gifticons 상세 조회 API")
  @GetMapping("/api/gifticon/{id}")
  public ResponseEntity<ApiResponse<GifticonDto.Gifticon>> getGifticon(@PathVariable Long id) {
    GifticonQuery.GetGifticon query = GifticonQuery.GetGifticon.builder()
            .gifticonId(id)
            .build();
    GifticonDto.Gifticon foundGifticon = gifticonQueryHandler.getGifticon(query);
    return ResponseEntity.ok(ApiResponse.success(foundGifticon, "기프티콘 상세 정보를 성공적으로 조회했습니다."));
  }

  @Operation(summary = "Gifticons 검색", description = "Gifticons 감섹 API")
  @GetMapping("/api/gifticon")
  public ResponseEntity<ApiResponse<GifticonListResponse>> getGifticons(@Valid @ParameterObject GifticonListRequest request) {
    GifticonQuery.ListGifticons query = mapper.toGifticonDtoListRequest(request);
    return ResponseEntity.ok(ApiResponse.success(
            gifticonQueryHandler.getGifticons(query),
            "기프티콘 목록을 성공적으로 조회했습니다."
    ));
  }

  @Operation(summary = "Gifticons 등록", description = "Gifticons 등록 API")
  @PostMapping("/api/gifticon")
  public ResponseEntity<ApiResponse<GifticonDto.Gifticon>> createGifticon(@RequestBody GifticonCreateRequest request) {
    GifticonCommand.CreateGifticon command = mapper.toCreateCommand(request);
    GifticonDto.Gifticon createdGifticon = gifticonCommandHandler.createGifticon(command);
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(createdGifticon, "기프티콘이 성공적으로 등록되었습니다."));
  }

  @Operation(summary = "Gifticons 거래", description = "Gifticons 거래 API")
  @PostMapping("/api/gifticon/{id}/trade")
  public ResponseEntity<ApiResponse<GifticonDto.Gifticon>> tradeGifticon(@PathVariable Long id, @RequestBody GifticonTradeRequest request) {
    GifticonCommand.TradeGifticon command = GifticonCommand.TradeGifticon.builder().gifticonId(id).userId(request.getUserId()).build();
    GifticonDto.Gifticon createdGifticon = gifticonCommandHandler.tradeGifticon(command);
    return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(createdGifticon, "기프티콘이 성공적으로 등록되었습니다."));
  }

  @Operation(summary = "Gifticons 삭제", description = "Gifticons 삭제 API")
  @DeleteMapping("/api/gifticon/{id}")
  public ResponseEntity<?> deleteGifticon(@PathVariable Long id) {
    GifticonCommand.DeleteGifticon command = GifticonCommand.DeleteGifticon.builder().gifticonId(id).build();
    gifticonCommandHandler.deleteGifticon(command);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
