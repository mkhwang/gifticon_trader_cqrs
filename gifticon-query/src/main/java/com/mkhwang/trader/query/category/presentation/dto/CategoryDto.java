package com.mkhwang.trader.query.category.presentation.dto;

import com.mkhwang.trader.common.dto.PaginationDto;
import com.mkhwang.trader.query.gifticon.application.dto.GifticonQueryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class CategoryDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Category {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private Integer level;
    private String imageUrl;
    @Builder.Default
    private List<Category> children = new ArrayList<>();
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Detail {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private Integer level;
    private String imageUrl;
  }

  @Data
  @Builder
  public static class ParentCategory {
    private Long id;
    private String name;
    private String slug;
  }

  @Data
  @Builder
  public static class CategoryGifticon {
    private Detail category;
    private List<GifticonQueryDto.GifticonSummary> items;
    private PaginationDto.PaginationInfo pagination;
  }
}