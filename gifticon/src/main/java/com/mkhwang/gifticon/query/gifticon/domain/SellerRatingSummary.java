package com.mkhwang.gifticon.query.gifticon.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerRatingSummary {

  private Long userId;
  private Double averageRating;
  private Integer totalCount;
  private Map<Integer, Integer> distribution;
}
