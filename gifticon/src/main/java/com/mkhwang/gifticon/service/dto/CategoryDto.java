package com.mkhwang.gifticon.service.dto;

import com.mkhwang.gifticon.service.gifticon.GifticonDto;
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
    public static class Detail {
        private Long id;
        private String name;
        private String slug;
        private String description;
        private Integer level;
        private String imageUrl;
        private ParentCategory parent;
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
        private List<GifticonDto.GifticonSummary> items;
        private PaginationDto.PaginationInfo pagination;
    }
}