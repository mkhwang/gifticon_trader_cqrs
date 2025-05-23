package com.mkhwang.gifticon.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewCreateRequest {
    private Integer rating;
    private String title;
    private String content;
}