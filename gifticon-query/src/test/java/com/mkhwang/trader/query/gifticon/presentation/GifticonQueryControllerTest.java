package com.mkhwang.trader.query.gifticon.presentation;

import com.mkhwang.trader.common.dto.PaginationDto;
import com.mkhwang.trader.query.gifticon.application.GifticonQueryHandler;
import com.mkhwang.trader.query.gifticon.application.dto.GifticonQueryDto;
import com.mkhwang.trader.query.gifticon.application.query.GifticonQuery;
import com.mkhwang.trader.query.gifticon.presentation.dto.GifticonListRequest;
import com.mkhwang.trader.query.gifticon.presentation.dto.GifticonListResponse;
import com.mkhwang.trader.query.gifticon.presentation.mapper.GiftionQueryMapper;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebMvcTest(controllers = GifticonQueryController.class)
class GifticonQueryControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @MockitoBean
  private GifticonQueryHandler gifticonQueryHandler;
  @MockitoBean
  private GiftionQueryMapper giftionQueryMapper;

  @DisplayName("기프티콘 단일 조회 성공시 http 200")
  @Test
  void getGifticon() throws Exception {
    // given
    Long gifticonId = 1L;
    GifticonQuery.GetGifticon query = mock(GifticonQuery.GetGifticon.class);
    given(giftionQueryMapper.toGetGifticonDetailQuery(gifticonId)).willReturn(query);
    GifticonQueryDto.Gifticon response = mock(GifticonQueryDto.Gifticon.class);
    given(gifticonQueryHandler.getGifticon(any())).willReturn(response);

    // when & then
    mockMvc.perform(get("/api/gifticon/" + gifticonId)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.message").value("기프티콘 상세 정보를 성공적으로 조회했습니다."));

    verify(gifticonQueryHandler).getGifticon(any());
  }

  @DisplayName("기프티콘 목록 조회 성공시 http 200")
  @Test
  void getGifticons() throws Exception {
    // given
    GifticonListRequest request = new GifticonListRequest();
    request.setPage(1);
    request.setPerPage(10);
    request.setSort("createdAt:desc");

    GifticonQuery.ListGifticons mockQuery = GifticonQuery.ListGifticons.builder()
            .pagination(
                    PaginationDto.PaginationRequest.builder().page(1).size(10).sort("createdAt:desc").build()
            ).build();

    GifticonListResponse mockResponse = new GifticonListResponse();

    given(giftionQueryMapper.toGifticonListQuery(any())).willReturn(mockQuery);
    given(gifticonQueryHandler.getGifticons(mockQuery)).willReturn(mockResponse);

    // when & then
    mockMvc.perform(get("/api/gifticon")
                    .param("page", "1")
                    .param("size", "10")
                    .param("sort", "createdAt:desc")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("기프티콘 목록을 성공적으로 조회했습니다."))
            .andExpect(jsonPath("$.data").exists());
  }
}