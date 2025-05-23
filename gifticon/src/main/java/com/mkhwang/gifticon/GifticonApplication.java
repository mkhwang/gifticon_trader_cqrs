package com.mkhwang.gifticon;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@OpenAPIDefinition(info = @Info(title = "Gifticon CQRS API", version = "0.0",
        description = "Gifticon CQRS service"))
@SpringBootApplication
@ConfigurationPropertiesScan
@EnableJpaAuditing
public class GifticonApplication {

  public static void main(String[] args) {
    SpringApplication.run(GifticonApplication.class, args);
  }

}
