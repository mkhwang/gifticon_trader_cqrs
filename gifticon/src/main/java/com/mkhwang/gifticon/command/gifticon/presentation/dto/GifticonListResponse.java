package com.mkhwang.gifticon.command.gifticon.presentation.dto;

import com.mkhwang.gifticon.common.dto.PaginationDto;
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

  private List<GifticonDto.GifticonSummary> items;
  private PaginationDto.PaginationInfo pagination;
}
