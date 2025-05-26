package com.mkhwang.gifticon.query.gifticon.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(indexName = "gifticons")
@Setting(settingPath = "elasticsearch/gifticon-index-settings.json")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GifticonSearchDocument {

  @Id
  private Long id;

  @Field(type = FieldType.Text, analyzer = "nori_analyzer")
  private String name;

  @Field(type = FieldType.Text, analyzer = "nori_analyzer")
  private String description;

  @Field(type = FieldType.Keyword)
  private String status;

  @Field(type = FieldType.Double)
  private BigDecimal basePrice;

  @Field(type = FieldType.Double)
  private BigDecimal salePrice;

  @Field(type = FieldType.Keyword)
  private String category;

  @Field(type = FieldType.Keyword)
  private String seller;

  @Field(type = FieldType.Long)
  private Long sellerId;

  @Field(type = FieldType.Keyword)
  private String buyer;

  @Field(type = FieldType.Keyword)
  private String brand;

  @Field(type = FieldType.Keyword)
  @Builder.Default
  private List<String> tags = new ArrayList<>();

  @Field(type = FieldType.Date, format = DateFormat.epoch_millis)
  private Instant createdAt;

  @Field(type = FieldType.Date, format = DateFormat.epoch_millis)
  private Instant updatedAt;

  @Field(type = FieldType.Keyword)
  private String slug;
}
