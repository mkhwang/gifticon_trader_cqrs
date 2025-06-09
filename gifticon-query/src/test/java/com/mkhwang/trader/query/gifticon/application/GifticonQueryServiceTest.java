package com.mkhwang.trader.query.gifticon.application;

import com.mkhwang.trader.common.dto.PaginationDto;
import com.mkhwang.trader.query.gifticon.application.dto.GifticonQueryDto;
import com.mkhwang.trader.query.gifticon.application.query.GifticonQuery;
import com.mkhwang.trader.query.gifticon.domain.GifticonDocument;
import com.mkhwang.trader.query.gifticon.domain.GifticonSearchDocument;
import com.mkhwang.trader.query.gifticon.infra.GifticonDocumentRepository;
import com.mkhwang.trader.query.gifticon.infra.GifticonSearchRepository;
import com.mkhwang.trader.query.gifticon.presentation.dto.GifticonListResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import support.AbstractIntegrationTest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
class GifticonQueryServiceTest extends AbstractIntegrationTest {

  @Autowired
  GifticonQueryService gifticonQueryService;

  @Autowired
  GifticonDocumentRepository gifticonDocumentRepository;

  @Autowired
  GifticonSearchRepository gifticonSearchRepository;

  @BeforeEach
  void setUp() {
    GifticonDocument document1 = GifticonDocument.builder().id(1L).name("name1").slug("slug")
            .description("description")
            .status("ON_SALE")
            .price(Map.of("basePrice", "10000", "salePrice", "10000"))
            .seller(Map.of("id", 1L, "nickname", "user1"))
            .brand(Map.of("id", 1L, "name", "starbucks"))
            .seller(Map.of("id", 1L))
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    GifticonDocument document2 = GifticonDocument.builder().id(2L).name("name2").slug("slug")
            .description("description")
            .status("ON_SALE")
            .brand(Map.of("id", 1L, "name", "starbucks"))
            .seller(Map.of("id", 2L, "nickname", "user2"))
            .price(Map.of("basePrice", "10000", "salePrice", "10000"))
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    gifticonDocumentRepository.saveAll(List.of(document1, document2));

    GifticonSearchDocument searchDocument1 = GifticonSearchDocument.builder().id(1L).name("name1")
            .slug("slug")
            .description("description")
            .status("ON_SALE")
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
    GifticonSearchDocument searchDocument2 = GifticonSearchDocument.builder().id(2L).name("name2")
            .slug("slug")
            .description("description")
            .status("ON_SALE")
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();

    gifticonSearchRepository.saveAll(List.of(searchDocument1, searchDocument2));
  }

  @DisplayName("기프티콘 단일조회 테스트")
  @Test
  void getGifticon() {
    // given
    Long gifticonId = 1L;
    GifticonQuery.GetGifticon query = GifticonQuery.GetGifticon.builder().gifticonId(gifticonId).build();

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
    GifticonQuery.ListGifticons query = GifticonQuery.ListGifticons.builder()
            .pagination(PaginationDto.PaginationRequest.builder().page(1).size(10).sort("createdAt:desc").build()).build();

    // when
    GifticonListResponse gifticons = gifticonQueryService.getGifticons(query);

    // then
    assertNotNull(gifticons);
    assertNotNull(gifticons.getItems());
    assertEquals(2, gifticons.getItems().size());
    assertEquals(1L, gifticons.getItems().get(0).getId());
    assertEquals(0, gifticons.getPagination().getCurrentPage());
    assertEquals(2, gifticons.getPagination().getTotalItems());

  }
}