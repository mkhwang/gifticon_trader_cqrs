package com.mkhwang.gifticon.command.gifticon.presentation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GifticonListRequest {
  private Integer page = 1;
  private Integer perPage = 10;
  private String sort = "createdAt:desc";
  private String status;
  private BigDecimal minPrice;
  private BigDecimal maxPrice;
  private List<Long> category;
  private Long seller;
  private Long brand;
  private Boolean inStock;
  private String search;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate createdFrom;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate createdTo;

}
