package com.mkhwang.trader.query.gifticon.application;

import com.mkhwang.trader.query.gifticon.application.dto.GifticonQueryDto;
import com.mkhwang.trader.query.gifticon.application.query.GifticonQuery;
import com.mkhwang.trader.query.gifticon.domain.GifticonDocument;
import com.mkhwang.trader.query.gifticon.infra.GifticonDocumentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import support.AbstractIntegrationTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
class GifticonQueryServiceTest extends AbstractIntegrationTest {

  @Autowired
  GifticonQueryService gifticonQueryService;

  @Autowired
  GifticonDocumentRepository gifticonDocumentRepository;

  @DisplayName("기프티콘 단일조회 테스트")
  @Test
  void getGifticon() {
    // given
    Long gifticonId = 1L;
    GifticonQuery.GetGifticon query = GifticonQuery.GetGifticon.builder().gifticonId(gifticonId).build();
    // given
    GifticonDocument document1 = GifticonDocument.builder().id(1L).name("name1").slug("slug")
            .description("description")
            .status("ON_SALE")
            .seller(Map.of("id", 1L))
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    GifticonDocument document2 = GifticonDocument.builder().id(2L).name("name2").slug("slug")
            .description("description")
            .status("ON_SALE")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    gifticonDocumentRepository.saveAll(List.of(document1, document2));

    // when
    GifticonQueryDto.Gifticon found = gifticonQueryService.getGifticon(query);

    // then
    assertNotNull(found);
    assertEquals(1L, found.getId());
    assertEquals("name1", found.getName());
  }

  @DisplayName("기프티콘 목록조회 테스트")
  @Test
  void getGifticons() {
    // given

    // when

    // then
  }
}