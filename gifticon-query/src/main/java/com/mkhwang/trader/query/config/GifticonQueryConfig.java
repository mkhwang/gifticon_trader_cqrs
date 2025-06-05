package com.mkhwang.trader.query.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = {
        "com.mkhwang.trader.query",
        "com.mkhwang.trader.common"
})
@EnableJpaRepositories(basePackages = "com.mkhwang.trader.common")
@EntityScan(basePackages = "com.mkhwang.trader.common")
@EnableJpaAuditing
public class GifticonQueryConfig {
}
