package com.mkhwang.gifticon.command.gifticon.presentation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GifticonCreateRequest {
    private String name;
    private String slug;
    private String description;
    private Long sellerId;
    private Long brandId;
    private Long categoryId;

    private GifticonPriceDto price;
    private List<ImageDto> images = new ArrayList<>();
    private List<Long> tagIds = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GifticonPriceDto {
        private BigDecimal basePrice;
        private BigDecimal salePrice;
        private String currency;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GifticonCategory {
        private Long categoryId;
    }

    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ImageDto {
        private String url;
        private String altText;
        private boolean isPrimary;
        private Integer displayOrder;
        private Long optionId;
    }
}