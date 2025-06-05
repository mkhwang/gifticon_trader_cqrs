package com.mkhwang.trader.query.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.mkhwang.trader.common")
@EntityScan(basePackages = "com.mkhwang.trader.common")
public class TestJpaConfig {
}
