package com.mkhwang.trader.query.gifticon.infra;

import com.mkhwang.trader.query.GifticonQueryApplication;
import com.mkhwang.trader.query.config.TestJpaConfig;
import com.mkhwang.trader.query.config.TestQueryDslConfig;
import com.mkhwang.trader.query.gifticon.domain.GifticonDocument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ActiveProfiles("test")
@SpringBootTest(classes = {GifticonQueryApplication.class})
@Testcontainers
class GifticonDocumentRepositoryTest {

  @Container
  static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

  @DynamicPropertySource
  static void mongoProps(DynamicPropertyRegistry registry) {
    registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
  }

  @Autowired
  private GifticonDocumentRepository repository;

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
    repository.saveAll(List.of(document1, document2));

    // when
    List<GifticonDocument> found = repository.findByIdIn(List.of(document1.getId(), document2.getId()));

    // then
    assertNotNull(found);
    assertEquals(2, found.size());
  }
}