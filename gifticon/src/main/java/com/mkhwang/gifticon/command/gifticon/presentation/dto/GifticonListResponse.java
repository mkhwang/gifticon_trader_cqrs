package com.mkhwang.gifticon.command.gifticon.presentation.dto;

import com.mkhwang.gifticon.common.dto.PaginationDto;
import com.mkhwang.gifticon.command.gifticon.application.GifticonDto;
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
