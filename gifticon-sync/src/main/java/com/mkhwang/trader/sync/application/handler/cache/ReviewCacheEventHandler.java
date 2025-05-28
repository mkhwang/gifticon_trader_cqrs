package com.mkhwang.trader.sync.application.handler.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkhwang.trader.query.gifticon.domain.UserRatingSummary;
import com.mkhwang.trader.query.review.infra.ReviewQueryRepository;
import com.mkhwang.trader.query.review.presentation.dto.ReviewDto;
import com.mkhwang.trader.sync.application.handler.AbstractCdcEventHandler;
import com.mkhwang.trader.sync.application.handler.dto.CdcEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class ReviewCacheEventHandler extends AbstractCdcEventHandler {
  private final RedisTemplate<String, UserRatingSummary> userRatingSummaryRedisTemplate;
  private final ReviewQueryRepository reviewQueryRepository;

  public ReviewCacheEventHandler(ObjectMapper objectMapper,
                                 RedisTemplate<String, UserRatingSummary> userRatingSummaryRedisTemplate,
                                 ReviewQueryRepository reviewQueryRepository) {
    super(objectMapper);
    this.userRatingSummaryRedisTemplate = userRatingSummaryRedisTemplate;
    this.reviewQueryRepository = reviewQueryRepository;
  }

  @Override
  protected String getSupportedTable() {
    return "reviews";
  }

  @Override
  public void handle(CdcEvent event) {
    if (event.isDelete()) {
      handleDelete(event);
    } else {
      handleCreateOrUpdate(event);
    }
  }

  private void handleDelete(CdcEvent event) {
    Map<String, Object> data = event.getBeforeData();
    if (data == null || !data.containsKey("user_id")) {
      return;
    }

    Long user_id = getLongValue(data, "user_id");
    userRatingSummaryRedisTemplate.delete("user:summary:" + user_id);
    log.info("Deleted user summary cache: {}", user_id);
  }

  private void handleCreateOrUpdate(CdcEvent event) {
    Map<String, Object> data = event.getAfterData();
    if (data == null || !data.containsKey("user_id")) {
      return;
    }

    Long userId = getLongValue(data, "user_id");
    ReviewDto.ReviewSummary userReviewSummary = reviewQueryRepository.getUserReviewSummary(userId);

    UserRatingSummary summary = UserRatingSummary.builder()
            .id(userId)
            .averageRating(userReviewSummary.getAverageRating())
            .totalCount(userReviewSummary.getTotalCount())
            .distribution(userReviewSummary.getDistribution())
            .build();

    userRatingSummaryRedisTemplate.opsForValue().set("user:summary:" + userId, summary);
    log.info("Updated user summary cache: {}", userId);
  }
}
