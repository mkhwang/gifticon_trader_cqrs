package com.mkhwang.trader.sync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

@ConfigurationPropertiesScan
@ComponentScan(basePackages = {
        "com.mkhwang.trader.sync",
        "com.mkhwang.trader.common",
        "com.mkhwang.trader.query"
})
@EntityScan(basePackages = "com.mkhwang.trader.common")
@SpringBootApplication
public class GifticonSyncApplication {

  public static void main(String[] args) {
    SpringApplication.run(GifticonSyncApplication.class, args);
  }

}
