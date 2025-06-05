package com.mkhwang.trader.query;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@OpenAPIDefinition(info = @Info(title = "Gifticon CQRS API", version = "0.0",
        description = "Gifticon Trader Query service"))
@SpringBootApplication
@ConfigurationPropertiesScan
public class GifticonQueryApplication {

  public static void main(String[] args) {
    SpringApplication.run(GifticonQueryApplication.class, args);
  }

}
