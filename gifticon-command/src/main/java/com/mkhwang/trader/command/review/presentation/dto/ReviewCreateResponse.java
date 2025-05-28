package com.mkhwang.trader.command.review.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCreateResponse {
  private Long id;
  private Long gifticonId;
  private Long reviewerId;
  private String title;
  private String content;
  private int rating;
}
