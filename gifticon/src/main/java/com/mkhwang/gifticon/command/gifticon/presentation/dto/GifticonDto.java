package com.mkhwang.gifticon.command.gifticon.presentation.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

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
    private Map<Integer, Integer> distribution;
  }

  @Data
  @NoArgsConstructor
  @Builder
  public static class Seller {
    private Long id;
    private String nickname;

    @QueryProjection
    public Seller(Long id, String nickname) {
      this.id = id;
      this.nickname = nickname;
    }
  }


  @Data
  @NoArgsConstructor
  @Builder
  public static class Brand {
    private Long id;
    private String name;

    @QueryProjection
    public Brand(Long id, String name) {
      this.id = id;
      this.name = name;
    }
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
  @Builder
  public static class Image {
    private Long id;
    private String url;
    private String altText;
    private boolean isPrimary;
    private Integer displayOrder;

    @QueryProjection
    public Image(Long id, String url, String altText, boolean isPrimary, Integer displayOrder) {
      this.id = id;
      this.url = url;
      this.altText = altText;
      this.isPrimary = isPrimary;
      this.displayOrder = displayOrder;
    }
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
  @Getter
  @NoArgsConstructor
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

    @QueryProjection
    public GifticonSummary(Long id, String name, String description, BigDecimal basePrice,
                           BigDecimal salePrice, String currency, Image primaryImage, Brand brand, Seller seller,
                           String status, LocalDateTime createdAt) {
      this.id = id;
      this.name = name;
      this.description = description;
      this.basePrice = basePrice;
      this.salePrice = salePrice;
      this.currency = currency;
      this.primaryImage = primaryImage;
      this.brand = brand;
      this.seller = seller;
      this.status = status;
      this.createdAt = createdAt;
    }
  }
}