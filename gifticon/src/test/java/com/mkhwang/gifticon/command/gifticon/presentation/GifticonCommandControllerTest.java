package com.mkhwang.gifticon.command.gifticon.presentation;

import com.mkhwang.gifticon.command.gifticon.application.command.GifticonCommand;
import com.mkhwang.gifticon.command.gifticon.application.usecase.CreateGifticonUseCase;
import com.mkhwang.gifticon.command.gifticon.application.usecase.DeleteGifticonUseCase;
import com.mkhwang.gifticon.command.gifticon.application.usecase.TradeGifticonUseCase;
import com.mkhwang.gifticon.command.gifticon.presentation.dto.GifticonCreateRequest;
import com.mkhwang.gifticon.command.gifticon.presentation.dto.GifticonDto;
import com.mkhwang.gifticon.command.gifticon.presentation.dto.GifticonTradeRequest;
import com.mkhwang.gifticon.command.gifticon.presentation.mapper.GifticonCommandMapper;
import com.mkhwang.gifticon.common.dto.ApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class GifticonCommandControllerTest {

  @Mock
  CreateGifticonUseCase createGifticonUseCase;
  @Mock
  DeleteGifticonUseCase deleteGifticonUseCase;
  @Mock
  TradeGifticonUseCase tradeGifticonUseCase;
  @Mock
  GifticonCommandMapper gifticonCommandMapper;
  @InjectMocks
  GifticonCommandController gifticonCommandController;

  @DisplayName("기프티콘 등록 Controller 테스트")
  @Test
  void create_gifticon() {
    // Given
    GifticonCreateRequest request = mock(GifticonCreateRequest.class);
    GifticonCommand.CreateGifticon command = mock(GifticonCommand.CreateGifticon.class);
    GifticonDto.Gifticon createdGifticon = mock(GifticonDto.Gifticon.class);
    given(gifticonCommandMapper.toCreateCommand(request)).willReturn(command);
    given(createGifticonUseCase.createGifticon(command)).willReturn(createdGifticon);

    // When
    ResponseEntity<ApiResponse<GifticonDto.Gifticon>> response =
            gifticonCommandController.createGifticon(request);

    // Then
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("기프티콘이 성공적으로 등록되었습니다.", response.getBody().getMessage());
  }

  @DisplayName("기프티콘 거래 Controller 테스트")
  @Test
  void trade_gifticon() {
    // Given
    GifticonTradeRequest request = mock(GifticonTradeRequest.class);
    Long gifticonId = 1L;
    GifticonCommand.TradeGifticon command = mock(GifticonCommand.TradeGifticon.class);
    GifticonDto.Gifticon gifticon = mock(GifticonDto.Gifticon.class);
    given(request.getUserId()).willReturn(2L);
    given(gifticonCommandMapper.toTradeCommand(gifticonId, request.getUserId())).willReturn(command);
    given(tradeGifticonUseCase.tradeGifticon(command)).willReturn(gifticon);

    // When
    ResponseEntity<ApiResponse<GifticonDto.Gifticon>> response = gifticonCommandController.tradeGifticon(gifticonId, request);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("기프티콘 거래가 완료되었습니다.", response.getBody().getMessage());
  }

  @DisplayName("기프티콘 삭제 Controller 테스트")
  @Test
  void delete_gifticon() {
    // Given
    Long gifticonId = 1L;
    GifticonCommand.DeleteGifticon command = mock(GifticonCommand.DeleteGifticon.class);
    given(gifticonCommandMapper.toDeleteCommand(gifticonId)).willReturn(command);

    // When
    ResponseEntity<?> response = gifticonCommandController.deleteGifticon(gifticonId);

    // Then
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    assertNull(response.getBody());
  }
}