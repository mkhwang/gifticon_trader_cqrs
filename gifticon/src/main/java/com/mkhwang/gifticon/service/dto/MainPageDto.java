package com.mkhwang.gifticon.service.dto;

import com.mkhwang.gifticon.service.gifticon.GifticonDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class MainPageDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MainPage {
        private List<GifticonDto.GifticonSummary> newGifticons;
        private List<GifticonDto.GifticonSummary> popularGifticons;
        private List<FeaturedCategory> featuredCategories;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FeaturedCategory {
        private Long id;
        private String name;
        private String slug;
        private String imageUrl;
        private Integer gifticonCount;
    }
}