package com.mkhwang.trader.command.review.presentation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewCreateRequest {
  private Long gifticonId;
  private Integer rating;
  private String title;
  private String content;
  private Long reviewerId;
}