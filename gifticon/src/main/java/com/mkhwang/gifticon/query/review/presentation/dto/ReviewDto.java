package com.mkhwang.gifticon.query.review.presentation.dto;

import com.mkhwang.gifticon.common.dto.PaginationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ReviewDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateRequest {
        private Integer rating;
        private String title;
        private String content;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequest {
        private Integer rating;
        private String title;
        private String content;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Review {
        private Long id;
        private User user;
        private Integer rating;
        private String title;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class User {
        private Long id;
        private String name;
        private String profileImageUrl;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReviewSummary {
        private Double averageRating;
        private Integer totalCount;
        private Map<Integer, Integer> distribution; // 평점별 개수
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReviewPage {
        private List<Review> items;
        private ReviewSummary summary;
        private PaginationDto.PaginationInfo pagination;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FlatRatingStatDto {
        private Double average;
        private Integer count;
        private Integer rating1;
        private Integer rating2;
        private Integer rating3;
        private Integer rating4;
        private Integer rating5;
    }
}