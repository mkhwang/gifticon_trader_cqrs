package com.mkhwang.trader.command.gifticon.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkhwang.trader.command.gifticon.application.mapper.GifticonCommandResponseMapper;
import com.mkhwang.trader.command.gifticon.application.usecase.CreateGifticonUseCase;
import com.mkhwang.trader.command.gifticon.application.usecase.DeleteGifticonUseCase;
import com.mkhwang.trader.command.gifticon.application.usecase.TradeGifticonUseCase;
import com.mkhwang.trader.command.gifticon.presentation.dto.GifticonCreateRequest;
import com.mkhwang.trader.command.gifticon.presentation.dto.GifticonDto;
import com.mkhwang.trader.command.gifticon.presentation.dto.GifticonTradeRequest;
import com.mkhwang.trader.command.gifticon.presentation.mapper.GifticonCommandMapper;
import com.mkhwang.trader.command.review.application.usecase.CreateReviewUseCase;
import com.mkhwang.trader.common.brand.infra.BrandRepository;
import com.mkhwang.trader.common.category.infra.CategoryRepository;
import com.mkhwang.trader.common.gifticon.infra.GifticonRepository;
import com.mkhwang.trader.common.tag.infra.TagRepository;
import com.mkhwang.trader.common.user.infra.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebMvcTest(controllers = GifticonCommandController.class)
class GifticonCommandControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private CreateGifticonUseCase createGifticonUseCase;

  @MockitoBean
  private CreateReviewUseCase createReviewUseCase;

  @MockitoBean
  private DeleteGifticonUseCase deleteGifticonUseCase;

  @MockitoBean
  private TradeGifticonUseCase tradeGifticonUseCase;

  @MockitoBean
  private GifticonCommandMapper gifticonCommandMapper;

  @MockitoBean
  private GifticonRepository gifticonRepository;

  @MockitoBean
  private UserRepository userRepository;

  @MockitoBean
  private BrandRepository brandRepository;

  @MockitoBean
  private CategoryRepository categoryRepository;

  @MockitoBean
  private GifticonCommandResponseMapper gifticonCommandResponseMapper;

  @MockitoBean
  private TagRepository tagRepository;


  @DisplayName("기프티콘 등록 성공 시, http 201")
  @Test
  void createGifticon_shouldReturn201() throws Exception {
    // given
    GifticonCreateRequest request = new GifticonCreateRequest();
    GifticonDto.Gifticon response = new GifticonDto.Gifticon();

    given(createGifticonUseCase.createGifticon(any())).willReturn(response);

    // when
    mockMvc.perform(post("/api/gifticon")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(request)))
            // then
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.message").value("기프티콘이 성공적으로 등록되었습니다."));

    verify(createGifticonUseCase).createGifticon(any());
  }

  @DisplayName("기프티콘 거래완료시, http 200")
  @Test
  void tradeGifticon_shouldReturn200() throws Exception {
    GifticonTradeRequest request = new GifticonTradeRequest();
    request.setUserId(1L);
    GifticonDto.Gifticon response = new GifticonDto.Gifticon();

    given(tradeGifticonUseCase.tradeGifticon(any())).willReturn(response);

    mockMvc.perform(post("/api/gifticon/1/trade")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.message").value("기프티콘 거래가 완료되었습니다."));

    verify(tradeGifticonUseCase).tradeGifticon(any());
  }

  @DisplayName("기프티콘 삭제 완료 시, http 204")
  @Test
  void deleteGifticon_shouldReturn204() throws Exception {
    mockMvc.perform(delete("/api/gifticon/1"))
            .andExpect(status().isNoContent());

    verify(deleteGifticonUseCase).deleteGifticon(any());
  }
}