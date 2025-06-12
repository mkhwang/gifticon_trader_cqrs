package com.mkhwang.trader.sync;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import support.AbstractIntegrationTest;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
class GifticonSyncApplicationTests extends AbstractIntegrationTest {

  @Test
  void contextLoads() {
  }

}
