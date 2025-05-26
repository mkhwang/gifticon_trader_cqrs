package com.mkhwang.gifticon.query.gifticon.presentation.dto;

import com.mkhwang.gifticon.common.dto.PaginationDto;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class GifticonQuery {

  @Data
  @Builder
  public static class GetGifticon {
    private Long gifticonId;
  }

  @Data
  @Builder
  public static class ListGifticons {
    private String status;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<Long> category;
    private Long seller;
    private Long brand;
    private String search;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate createdTo;

    private PaginationDto.PaginationRequest pagination;
  }
}
