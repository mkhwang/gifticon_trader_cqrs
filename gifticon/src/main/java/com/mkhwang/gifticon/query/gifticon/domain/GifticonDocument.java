package com.mkhwang.gifticon.query.gifticon.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Document(collation = "gifticon")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GifticonDocument {
  @Id
  private Long id;
  private String name;
  private String slug;
  private String shortDescription;
  private String fullDescription;
  private String status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private Map<String, Object> seller;

  private Map<String, Object> brand;

  private Map<String, Object> price;

  private Map<String, Object> category;

  @Builder.Default
  private List<Map<String, Object>> images = new ArrayList<>();

  @Builder.Default
  private List<Map<String, Object>> tags = new ArrayList<>();
}
