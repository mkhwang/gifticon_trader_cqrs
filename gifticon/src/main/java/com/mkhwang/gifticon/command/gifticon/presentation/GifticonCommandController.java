package com.mkhwang.gifticon.command.gifticon.presentation;

import com.mkhwang.gifticon.command.gifticon.application.usecase.CreateGifticonUseCase;
import com.mkhwang.gifticon.command.gifticon.application.usecase.DeleteGifticonUseCase;
import com.mkhwang.gifticon.command.gifticon.application.usecase.TradeGifticonUseCase;
import com.mkhwang.gifticon.command.gifticon.presentation.dto.GifticonCreateRequest;
import com.mkhwang.gifticon.command.gifticon.presentation.dto.GifticonTradeRequest;
import com.mkhwang.gifticon.command.gifticon.presentation.mapper.GifticonCommandMapper;
import com.mkhwang.gifticon.command.gifticon.application.command.GifticonCommand;
import com.mkhwang.gifticon.common.dto.ApiResponse;

import com.mkhwang.gifticon.command.gifticon.presentation.dto.GifticonDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Gifticons", description = "Gifticons API")
@RestController
@RequiredArgsConstructor
public class GifticonCommandController {
  private final CreateGifticonUseCase createGifticonUseCase;
  private final DeleteGifticonUseCase deleteGifticonUseCase;
  private final TradeGifticonUseCase tradeGifticonUseCase;
  private final GifticonCommandMapper gifticonCommandMapper;

  @Operation(summary = "Gifticons 등록", description = "Gifticons 등록 API")
  @PostMapping("/api/gifticon")
  public ResponseEntity<ApiResponse<GifticonDto.Gifticon>> createGifticon(@RequestBody GifticonCreateRequest request) {
    GifticonCommand.CreateGifticon command = gifticonCommandMapper.toCreateCommand(request);
    GifticonDto.Gifticon createdGifticon = createGifticonUseCase.createGifticon(command);
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(createdGifticon, "기프티콘이 성공적으로 등록되었습니다."));
  }

  @Operation(summary = "Gifticons 거래", description = "Gifticons 거래 API")
  @PostMapping("/api/gifticon/{id}/trade")
  public ResponseEntity<ApiResponse<GifticonDto.Gifticon>> tradeGifticon(@PathVariable Long id, @RequestBody GifticonTradeRequest request) {
    GifticonDto.Gifticon gifticon = tradeGifticonUseCase.tradeGifticon(gifticonCommandMapper.toTradeCommand(id, request.getUserId()));
    return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(gifticon, "기프티콘이 성공적으로 등록되었습니다."));
  }

  @Operation(summary = "Gifticons 삭제", description = "Gifticons 삭제 API")
  @DeleteMapping("/api/gifticon/{id}")
  public ResponseEntity<?> deleteGifticon(@PathVariable Long id) {
    deleteGifticonUseCase.deleteGifticon(gifticonCommandMapper.toDeleteCommand(id));
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
