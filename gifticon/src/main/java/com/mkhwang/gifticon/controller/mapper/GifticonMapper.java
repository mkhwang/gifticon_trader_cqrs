package com.mkhwang.gifticon.controller.mapper;

import com.mkhwang.gifticon.controller.dto.GifticonCreateRequest;
import com.mkhwang.gifticon.controller.dto.GifticonListRequest;
import com.mkhwang.gifticon.service.dto.PaginationDto;
import com.mkhwang.gifticon.service.entity.Gifticon;
import com.mkhwang.gifticon.service.gifticon.GifticonCommand;
import com.mkhwang.gifticon.service.gifticon.GifticonDto;
import com.mkhwang.gifticon.service.query.GifticonQuery;
import org.springframework.stereotype.Component;

@Component
public class GifticonMapper {

  public GifticonQuery.ListGifticons toGifticonDtoListRequest(GifticonListRequest request) {
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

  public GifticonDto.Gifticon toDto(Gifticon gifticon) {
    return GifticonDto.Gifticon.builder()
            .id(gifticon.getId())
            .name(gifticon.getName())
            .slug(gifticon.getSlug())
            .description(gifticon.getDescription())
            .brand(GifticonDto.Brand.builder()
                    .id(gifticon.getBrand().getId())
                    .name(gifticon.getBrand().getName())
                    .build())
            .seller(
                    GifticonDto.Seller.builder()
                            .id(gifticon.getSeller().getId())
                            .nickname(gifticon.getSeller().getNickname())
                            .build())
            .status(gifticon.getStatus().toString())
            .createdAt(gifticon.getCreatedAt())
            .updatedAt(gifticon.getUpdatedAt())
            .price(
                    GifticonDto.Price.builder()
                            .basePrice(gifticon.getPrice().getBasePrice())
                            .salePrice(gifticon.getPrice().getSalePrice())
                            .currency(gifticon.getPrice().getCurrency())
                            .discountPercentage(gifticon.getPrice().getDiscountPercentage())
                            .build())
            .category(
                    GifticonDto.Category.builder()
                            .id(gifticon.getCategory().getId())
                            .name(gifticon.getCategory().getName())
                            .slug(gifticon.getCategory().getSlug())
                            .build())
            .images(
                    gifticon.getImages().stream()
                            .map(gifticonImage -> GifticonDto.Image.builder()
                                    .id(gifticonImage.getId())
                                    .url(gifticonImage.getUrl())
                                    .altText(gifticonImage.getAltText())
                                    .displayOrder(gifticonImage.getDisplayOrder())
                                    .isPrimary(gifticonImage.isPrimary())
                                    .build())
                            .toList())
            .tags(
                    gifticon.getTags().stream()
                            .map(tag -> GifticonDto.Tag.builder()
                                    .id(tag.getId())
                                    .name(tag.getName())
                                    .slug(tag.getSlug())
                                    .build())
                            .toList()).build();


  }

  public GifticonCommand.CreateGifticon toCreateCommand(GifticonCreateRequest request) {
    return GifticonCommand.CreateGifticon.builder()
            .brandId(request.getBrandId())
            .categoryId(request.getCategoryId())
            .description(request.getDescription())
            .name(request.getName())
            .slug(request.getSlug())
            .sellerId(request.getSellerId())
            .price(GifticonDto.Price.builder()
                    .basePrice(request.getPrice().getBasePrice())
                    .salePrice(request.getPrice().getSalePrice())
                    .currency(request.getPrice().getCurrency())
                    .build())
            .build();
  }
}
