package com.mkhwang.trader.sync.application.handler.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkhwang.trader.query.gifticon.domain.UserRatingSummary;
import com.mkhwang.trader.query.review.infra.ReviewQueryRepository;
import com.mkhwang.trader.query.review.presentation.dto.ReviewDto;
import com.mkhwang.trader.sync.application.handler.dto.CdcEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.junit.jupiter.Testcontainers;
import support.AbstractIntegrationTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
class ReviewCacheEventHandlerTest extends AbstractIntegrationTest {
  @Autowired
  ObjectMapper objectMapper;
  @MockitoBean
  RedisTemplate<String, UserRatingSummary> userRatingSummaryRedisTemplate;
  @MockitoBean
  ReviewQueryRepository reviewQueryRepository;
  @Autowired
  ReviewCacheEventHandler reviewCacheEventHandler;

  @DisplayName("리뷰 캐시 이벤트 핸들러 - 지원하는 테이블 확인")
  @Test
  void getSupportedTable() {
    // given
    String table = "reviews";

    // when
    String supportedTable = reviewCacheEventHandler.getSupportedTable();

    // then
    assertEquals(table, supportedTable);
  }

  @DisplayName("리뷰 캐시 이벤트 핸들러 - 삭제 이벤트 처리")
  @Test
  void handle_delete_event() {
    // given
    CdcEvent event = new CdcEvent();
    event.setOp("d");
    event.setBefore(Map.of("user_id", 1L));

    // when
    reviewCacheEventHandler.handle(event);

    // then
    verify(userRatingSummaryRedisTemplate).delete("user:summary:1");
  }

  @DisplayName("리뷰 캐시 이벤트 핸들러 - 생성/수정 이벤트 처리")
  @Test
  void handle_create_or_update_event() {
    // given
    CdcEvent event = new CdcEvent();
    Long userId = 1L;
    event.setOp("c");
    event.setAfter(Map.of("user_id", userId));
    ReviewDto.ReviewSummary reviewSummary = ReviewDto.ReviewSummary.builder().build();
    given(reviewQueryRepository.getUserReviewSummary(userId)).willReturn(reviewSummary);
    ValueOperations<String, UserRatingSummary> valueOps = mock(ValueOperations.class);
    given(userRatingSummaryRedisTemplate.opsForValue()).willReturn(valueOps);

    // when
    reviewCacheEventHandler.handle(event);

    // then
    verify(valueOps).set(eq("user:summary:1"), any(UserRatingSummary.class));
  }
}