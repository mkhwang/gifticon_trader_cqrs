package com.mkhwang.trader.command.review.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkhwang.trader.command.review.application.command.ReviewCommand;
import com.mkhwang.trader.command.review.application.mapper.ReviewCommandMapper;
import com.mkhwang.trader.command.review.application.usecase.CreateReviewUseCase;
import com.mkhwang.trader.command.review.presentation.dto.ReviewCreateRequest;
import com.mkhwang.trader.command.review.presentation.dto.ReviewCreateResponse;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebMvcTest(controllers = ReviewCommandController.class)
class ReviewCommandControllerIntegrationTest {
  @Autowired
  private MockMvc mockMvc;
  @MockitoBean
  private ReviewCommandMapper reviewCommandMapper;
  @MockitoBean
  private CreateReviewUseCase createReviewUseCase;


  @DisplayName("리뷰 등록 성공 테스트")
  @Test
  void createReview_성공() throws Exception {
    // given
    ReviewCreateRequest request = new ReviewCreateRequest();

    ReviewCommand.CreateReview command = new ReviewCommand.CreateReview();
    ReviewCreateResponse response = new ReviewCreateResponse();

    given(reviewCommandMapper.toCreateReviewCommand(any())).willReturn(command);
    given(createReviewUseCase.createReview(any())).willReturn(response);

    // when & then
    mockMvc.perform(post("/api/review")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").exists());
  }
}