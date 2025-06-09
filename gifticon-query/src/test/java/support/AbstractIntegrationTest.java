package support;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

public class AbstractIntegrationTest {
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
    registry.add("spring.mongodb.host", mongoDBContainer::getHost);
    registry.add("spring.mongodb.port", () -> mongoDBContainer.getMappedPort(27017));
    registry.add("spring.mongodb.username", () -> "admin");
    registry.add("spring.mongodb.password", () -> "admin123");
    registry.add("spring.mongodb.database", () -> "gifticon");
  }

  @DynamicPropertySource
  static void elasticsearchProps(DynamicPropertyRegistry registry) {
    registry.add("spring.elasticsearch.host", elasticsearchContainer::getHost);
    registry.add("spring.elasticsearch.port", () -> elasticsearchContainer.getMappedPort(9200));
  }
}
