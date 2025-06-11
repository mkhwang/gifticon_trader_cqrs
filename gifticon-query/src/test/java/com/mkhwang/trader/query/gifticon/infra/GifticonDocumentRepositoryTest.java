package com.mkhwang.trader.query.gifticon.infra;

import com.mkhwang.trader.query.gifticon.domain.GifticonDocument;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import support.AbstractIntegrationTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@Transactional
@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
class GifticonDocumentRepositoryTest extends AbstractIntegrationTest {

  @Autowired
  private GifticonDocumentRepository repository;

  @Disabled("MongoDB 연결 문제로 스킵")
  @Test
  void findByIdIn() {
    // given
    GifticonDocument document1 = GifticonDocument.builder().id(1L).name("name1").slug("slug")
            .description("description")
            .status("ON_SALE")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    GifticonDocument document2 = GifticonDocument.builder().id(2L).name("name2").slug("slug")
            .description("description")
            .status("ON_SALE")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    repository.save(document1);
    repository.save(document2);

    // when
    List<GifticonDocument> found = repository.findByIdIn(List.of(document1.getId(), document2.getId()));

    // then
    assertNotNull(found);
    assertEquals(2, found.size());
  }
}