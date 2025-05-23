package com.mkhwang.gifticon;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(title = "Gifticon CQRS API", version = "0.0",
        description = "Gifticon CQRS service"))
@SpringBootApplication
public class GifticonApplication {

  public static void main(String[] args) {
    SpringApplication.run(GifticonApplication.class, args);
  }

}
