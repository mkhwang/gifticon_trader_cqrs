package com.mkhwang.trader.query.gifticon.presentation.dto;

import com.mkhwang.trader.common.dto.PaginationDto;
import com.mkhwang.trader.query.gifticon.application.dto.GifticonQueryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GifticonListResponse {

  private List<GifticonQueryDto.GifticonSummary> items;
  private PaginationDto.PaginationInfo pagination;
}
