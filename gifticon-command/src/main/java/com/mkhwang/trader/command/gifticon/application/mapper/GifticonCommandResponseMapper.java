package com.mkhwang.trader.command.gifticon.application.mapper;

import com.mkhwang.trader.command.gifticon.presentation.dto.GifticonDto;
import com.mkhwang.trader.common.gifticon.domain.Gifticon;
import org.springframework.stereotype.Component;

@Component
public class GifticonCommandResponseMapper {

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


}
