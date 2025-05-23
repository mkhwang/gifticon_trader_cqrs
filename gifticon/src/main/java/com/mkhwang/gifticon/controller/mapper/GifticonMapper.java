package com.mkhwang.gifticon.controller.mapper;

import com.mkhwang.gifticon.controller.dto.GifticonListRequest;
import com.mkhwang.gifticon.service.dto.PaginationDto;
import com.mkhwang.gifticon.service.query.GifticonQuery;
import org.springframework.stereotype.Component;

@Component
public class GifticonMapper {

  public GifticonQuery.ListGifticons toProductDtoListRequest(GifticonListRequest request) {
    return GifticonQuery.ListGifticons.builder()
            .status(request.getStatus())
            .minPrice(request.getMinPrice())
            .maxPrice(request.getMaxPrice())
            .category(request.getCategory())
            .seller(request.getSeller())
            .brand(request.getBrand())
            .tag(request.getTag())
            .search(request.getSearch())
            .createdFrom(request.getCreatedFrom())
            .createdTo(request.getCreatedTo())
            .pagination(PaginationDto.PaginationRequest.builder()
                    .page(request.getPage())
                    .size(request.getPerPage())
                    .sort(request.getSort())
                    .build())
            .build();
  }
}
