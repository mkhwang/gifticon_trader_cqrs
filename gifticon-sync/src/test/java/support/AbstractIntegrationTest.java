package support;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

public class AbstractIntegrationTest {
  @Container
  static GenericContainer<?> mongoDBContainer = new GenericContainer<>("mongo:6.0")
          .withEnv("MONGO_INITDB_ROOT_USERNAME", "admin")
          .withEnv("MONGO_INITDB_ROOT_PASSWORD", "admin123")
          .withExposedPorts(27017)
          .waitingFor(Wait.forLogMessage(".*Waiting for connections.*\\n", 1))
          .withStartupTimeout(Duration.ofSeconds(60));


  @Container
  static ElasticsearchContainer elasticsearchContainer = new ElasticsearchContainer(
          DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:8.17.5"))
          .withEnv("xpack.security.enabled", "false")
          .withEnv("discovery.type", "single-node")
          .withExposedPorts(9200, 9300)
          .withCreateContainerCmdModifier(cmd -> {
            cmd.withEntrypoint("/bin/bash", "-c",
                    "bin/elasticsearch-plugin install analysis-nori || true && exec /usr/local/bin/docker-entrypoint.sh");
          })
          .waitingFor(Wait.forHealthcheck())
          .waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(60)));

  @Container
  static GenericContainer<?> redisContainer = new GenericContainer<>("redis:7.2")
          .withExposedPorts(6379)
          .waitingFor(Wait.forHealthcheck())
          .waitingFor(Wait.forLogMessage(".*Ready to accept connections.*", 1))
          .waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(60)));

  @DynamicPropertySource
  static void mongoProps(DynamicPropertyRegistry registry) {
    if (!mongoDBContainer.isRunning()) {
      mongoDBContainer.start();
    }
//    registry.add("spring.data.mongodb.uri", () -> String.format("mongodb://%s:%s@%s:%d", "admin", "admin123", mongoDBContainer.getHost(), mongoDBContainer.getMappedPort(27017)));

    registry.add("spring.mongodb.host", mongoDBContainer::getHost);
    registry.add("spring.mongodb.port", () -> mongoDBContainer.getMappedPort(27017));
    registry.add("spring.mongodb.username", () -> "admin");
    registry.add("spring.mongodb.password", () -> "admin123");
    registry.add("spring.mongodb.database", () -> "gifticon");
  }

  @DynamicPropertySource
  static void elasticsearchProps(DynamicPropertyRegistry registry) {
    if (!elasticsearchContainer.isRunning()) {
      elasticsearchContainer.start();
    }

    registry.add("spring.elasticsearch.host", elasticsearchContainer::getHost);
    registry.add("spring.elasticsearch.port", () -> elasticsearchContainer.getMappedPort(9200));
  }

  @DynamicPropertySource
  static void redisProps(DynamicPropertyRegistry registry) {
    if (!redisContainer.isRunning()) {
      redisContainer.start();
    }

    registry.add("spring.data.redis.host", redisContainer::getHost);
    registry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(6379));
  }
}
