package com.mkhwang.trader.command;

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
        description = "Gifticon Trader Command service"))
@SpringBootApplication
@ConfigurationPropertiesScan
@ComponentScan(basePackages = {
        "com.mkhwang.trader.command",
        "com.mkhwang.trader.common"
})
@EnableJpaRepositories(basePackages = "com.mkhwang.trader.common")
@EntityScan(basePackages = "com.mkhwang.trader.common")
@EnableJpaAuditing
public class GifticonCommandApplication {

  public static void main(String[] args) {
    SpringApplication.run(GifticonCommandApplication.class, args);
  }

}
