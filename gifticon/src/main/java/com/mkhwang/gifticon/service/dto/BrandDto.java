package com.mkhwang.gifticon.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class BrandDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Brand {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private String logoUrl;
    private String website;
  }
}
