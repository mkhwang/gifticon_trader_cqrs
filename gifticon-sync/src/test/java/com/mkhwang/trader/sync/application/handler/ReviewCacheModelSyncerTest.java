package com.mkhwang.trader.sync.application.handler;

import com.mkhwang.trader.sync.application.ReviewCacheModelSyncer;
import com.mkhwang.trader.sync.application.handler.cache.ReviewCacheEventHandler;
import com.mkhwang.trader.sync.application.handler.dto.CdcEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.junit.jupiter.Testcontainers;
import support.AbstractIntegrationTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
class ReviewCacheModelSyncerTest extends AbstractIntegrationTest {
  @MockitoBean
  ReviewCacheEventHandler reviewCacheEventHandler;

  @Autowired
  ReviewCacheModelSyncer reviewCacheModelSyncer;

  @DisplayName("리뷰 캐시 모델 동기화 테스트")
  @Test
  void consumeDocumentEvents() {
    // given
    String messageValue = "{\n" +
            "\t\"before\": null,\n" +
            "\t\"after\": {\n" +
            "\t\t\"id\": 1870,\n" +
            "\t\t\"gifticon_id\": 4711,\n" +
            "\t\t\"reviewer\": 986,\n" +
            "\t\t\"title\": \"가성비 최고예요\",\n" +
            "\t\t\"content\": \"기한도 넉넉하고 실물도 깔끔했어요.\",\n" +
            "\t\t\"rating\": 4,\n" +
            "\t\t\"created_at\": 1740849824000000,\n" +
            "\t\t\"updated_at\": 1740936224000000,\n" +
            "\t\t\"user_id\": 218\n" +
            "\t},\n" +
            "\t\"source\": {\n" +
            "\t\t\"version\": \"2.7.3.Final\",\n" +
            "\t\t\"connector\": \"postgresql\",\n" +
            "\t\t\"name\": \"review\",\n" +
            "\t\t\"ts_ms\": 1748186493993,\n" +
            "\t\t\"snapshot\": \"false\",\n" +
            "\t\t\"db\": \"gifticon\",\n" +
            "\t\t\"sequence\": \"[\\\"88750104\\\",\\\"88750424\\\"]\",\n" +
            "\t\t\"ts_us\": 1748186493993151,\n" +
            "\t\t\"ts_ns\": 1748186493993151000,\n" +
            "\t\t\"schema\": \"public\",\n" +
            "\t\t\"table\": \"reviews\",\n" +
            "\t\t\"txId\": 1748,\n" +
            "\t\t\"lsn\": 88750424,\n" +
            "\t\t\"xmin\": null\n" +
            "\t},\n" +
            "\t\"transaction\": null,\n" +
            "\t\"op\": \"u\",\n" +
            "\t\"ts_ms\": 1748186494159,\n" +
            "\t\"ts_us\": 1748186494159222,\n" +
            "\t\"ts_ns\": 1748186494159222346\n" +
            "}";
    String messageKey = "{\n" +
            "\t\"gifticon_id\": 1870\n" +
            "}";
    String topic = "review-events";

    // when
    reviewCacheModelSyncer.consumeDocumentEvents(messageValue, messageKey, topic);

    // then
    verify(reviewCacheEventHandler).canHandle(any(CdcEvent.class));
  }
}