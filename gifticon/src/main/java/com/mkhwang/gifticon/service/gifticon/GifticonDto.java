package com.mkhwang.gifticon.service.gifticon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class GifticonDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Gifticon {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private Seller seller;
    private Brand brand;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Price price;
    private Category category;
    private List<Image> images;
    private List<Tag> tags;
    private SellerSummary sellerSummary;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class SellerSummary {
    private Long id;
    private Integer reviewCount;
    private Double average;
    private Integer count;
    private Map<Integer, Integer> distribution;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Seller {
    private Long id;
    private String nickname;
  }


  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Brand {
    private Long id;
    private String name;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Price {
    private BigDecimal basePrice;
    private BigDecimal salePrice;
    private String currency;
    private Integer discountPercentage = 0;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Category {
    private Long id;
    private String name;
    private String slug;
    private ParentCategory parent;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class ParentCategory {
    private Long id;
    private String name;
    private String slug;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Image {
    private Long id;
    private String url;
    private String altText;
    private boolean isPrimary;
    private Integer displayOrder;
    private Long optionId;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Tag {
    private Long id;
    private String name;
    private String slug;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class GifticonSummary {
    private Long id;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private BigDecimal salePrice;
    private String currency;
    private Image primaryImage;
    private Brand brand;
    private Seller seller;
    private String status;
    private LocalDateTime createdAt;
  }
}