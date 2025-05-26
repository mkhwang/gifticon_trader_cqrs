package com.mkhwang.gifticon.query.gifticon.presentation.mapper;

import com.mkhwang.gifticon.command.gifticon.presentation.dto.GifticonListRequest;
import com.mkhwang.gifticon.common.dto.PaginationDto;
import com.mkhwang.gifticon.query.gifticon.presentation.dto.GifticonQuery;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class GiftionQueryMapper {

  public GifticonQuery.ListGifticons toGifticonListQuery(@Valid GifticonListRequest request) {
    return GifticonQuery.ListGifticons.builder()
            .status(request.getStatus())
            .minPrice(request.getMinPrice())
            .maxPrice(request.getMaxPrice())
            .category(request.getCategory())
            .seller(request.getSeller())
            .brand(request.getBrand())
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

  public GifticonQuery.GetGifticon toGetGifticonDetailQuery(Long gifticonId) {
    return GifticonQuery.GetGifticon.builder()
            .gifticonId(gifticonId)
            .build();
  }

}
