package com.mkhwang.trader.command.review.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ReviewCommand {

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CreateReview {
    private Long gifticonId;
    private Long reviewerId;
    private String title;
    private String content;
    private int rating;


  }
}
