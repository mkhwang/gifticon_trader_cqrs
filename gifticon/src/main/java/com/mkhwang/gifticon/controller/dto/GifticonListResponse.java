package com.mkhwang.gifticon.controller.dto;

import com.mkhwang.gifticon.service.dto.PaginationDto;
import com.mkhwang.gifticon.service.gifticon.GifticonDto;
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
