package com.mkhwang.trader.query.gifticon.infra;

import com.mkhwang.trader.query.gifticon.domain.GifticonDocument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
class GifticonDocumentRepositoryTest {

  @Container
  static GenericContainer<?> mongoDBContainer = new GenericContainer<>("mongo:8.0")
          .withEnv("MONGO_INITDB_ROOT_USERNAME", "admin")
          .withEnv("MONGO_INITDB_ROOT_PASSWORD", "admin123")
          .withExposedPorts(27017);

  @Container
  static ElasticsearchContainer elasticsearchContainer = new ElasticsearchContainer(
          DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:8.17.5"))
          .withEnv("xpack.security.enabled", "false")
          .withEnv("discovery.type", "single-node")
          .withExposedPorts(9200, 9300)
          .withCreateContainerCmdModifier(cmd -> {
            cmd.withEntrypoint("/bin/bash", "-c",
                    "bin/elasticsearch-plugin install analysis-nori || true && exec /usr/local/bin/docker-entrypoint.sh");
          });

  @DynamicPropertySource
  static void mongoProps(DynamicPropertyRegistry registry) {
    mongoDBContainer.start();
    registry.add("spring.mongodb.host", mongoDBContainer::getHost);
    registry.add("spring.mongodb.port", () -> mongoDBContainer.getMappedPort(27017));
    registry.add("spring.mongodb.username", () -> "admin");
    registry.add("spring.mongodb.password", () -> "admin123");
    registry.add("spring.mongodb.database", () -> "gifticon");
  }

  @DynamicPropertySource
  static void elasticsearchProps(DynamicPropertyRegistry registry) {
    elasticsearchContainer.start();
    registry.add("spring.elasticsearch.host", elasticsearchContainer::getHost);
    registry.add("spring.elasticsearch.port", () -> elasticsearchContainer.getMappedPort(9200));
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