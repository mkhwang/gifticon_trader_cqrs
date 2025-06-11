package com.mkhwang.trader.query.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableMongoRepositories(basePackages = "com.mkhwang.trader.query.gifticon.infra")
public class MongoDBConfig {

  @Value("${spring.mongodb.host}")
  private String host;

  @Value("${spring.mongodb.port}")
  private int port;

  @Value("${spring.mongodb.username}")
  private String username;

  @Value("${spring.mongodb.password}")
  private String password;

  @Value("${spring.mongodb.database}")
  private String database;

  @Bean
  public MongoClient mongoClient() {
    ConnectionString connectionString = new ConnectionString(String.format("mongodb://%s:%s@%s:%d",
            username, password, host, port));
    MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .applyToSocketSettings(builder -> builder
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
            )
            .build();

    return MongoClients.create(settings);
  }

  @Bean
  public MongoTemplate mongoTemplate() {
    return new MongoTemplate(mongoClient(), database);
  }
}