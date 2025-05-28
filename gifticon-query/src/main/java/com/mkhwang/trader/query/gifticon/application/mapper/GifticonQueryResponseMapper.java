package com.mkhwang.trader.query.gifticon.application.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkhwang.trader.common.gifticon.domain.Gifticon;
import com.mkhwang.trader.query.gifticon.domain.GifticonDocument;
import com.mkhwang.trader.query.gifticon.domain.UserRatingSummary;
import com.mkhwang.trader.query.gifticon.application.dto.GifticonQueryDto;
import com.mkhwang.trader.query.review.presentation.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GifticonQueryResponseMapper {
  private final ObjectMapper objectMapper;

  public GifticonQueryDto.Gifticon toGifticon(Gifticon gifticon, ReviewDto.ReviewSummary summary) {
    return GifticonQueryDto.Gifticon.builder()
            .id(gifticon.getId())
            .name(gifticon.getName())
            .slug(gifticon.getSlug())
            .description(gifticon.getDescription())
            .brand(GifticonQueryDto.Brand.builder()
                    .id(gifticon.getBrand().getId())
                    .name(gifticon.getBrand().getName())
                    .build())
            .seller(
                    GifticonQueryDto.Seller.builder()
                            .id(gifticon.getSeller().getId())
                            .nickname(gifticon.getSeller().getNickname())
                            .build())
            .status(gifticon.getStatus().toString())
            .createdAt(gifticon.getCreatedAt())
            .updatedAt(gifticon.getUpdatedAt())
            .price(
                    GifticonQueryDto.Price.builder()
                            .basePrice(gifticon.getPrice().getBasePrice())
                            .salePrice(gifticon.getPrice().getSalePrice())
                            .currency(gifticon.getPrice().getCurrency())
                            .discountPercentage(gifticon.getPrice().getDiscountPercentage())
                            .build())
            .category(
                    GifticonQueryDto.Category.builder()
                            .id(gifticon.getCategory().getId())
                            .name(gifticon.getCategory().getName())
                            .slug(gifticon.getCategory().getSlug())
                            .build())
            .images(
                    gifticon.getImages().stream()
                            .map(gifticonImage -> GifticonQueryDto.Image.builder()
                                    .id(gifticonImage.getId())
                                    .url(gifticonImage.getUrl())
                                    .altText(gifticonImage.getAltText())
                                    .displayOrder(gifticonImage.getDisplayOrder())
                                    .isPrimary(gifticonImage.isPrimary())
                                    .build())
                            .toList())
            .tags(
                    gifticon.getTags().stream()
                            .map(tag -> GifticonQueryDto.Tag.builder()
                                    .id(tag.getId())
                                    .name(tag.getName())
                                    .slug(tag.getSlug())
                                    .build())
                            .toList()
            ).sellerSummary(
                    GifticonQueryDto.SellerSummary.builder()
                            .average(summary.getAverageRating())
                            .reviewCount(summary.getTotalCount())
                            .distribution(summary.getDistribution())
                            .build()
            )
            .build();
  }


  public GifticonQueryDto.Gifticon toGifticon(GifticonDocument gifticon, UserRatingSummary summary) {
    return GifticonQueryDto.Gifticon.builder()
            .id(gifticon.getId())
            .name(gifticon.getName())
            .slug(gifticon.getSlug())
            .description(gifticon.getDescription())
            .brand(objectMapper.convertValue(gifticon.getBrand(), GifticonQueryDto.Brand.class))
            .seller(objectMapper.convertValue(gifticon.getSeller(), GifticonQueryDto.Seller.class))
            .status(gifticon.getStatus())
            .createdAt(gifticon.getCreatedAt())
            .updatedAt(gifticon.getUpdatedAt())
            .price(objectMapper.convertValue(gifticon.getPrice(), GifticonQueryDto.Price.class))
            .category(objectMapper.convertValue(gifticon.getCategory(), GifticonQueryDto.Category.class))
            .images(
                    gifticon.getImages().stream()
                            .map(gifticonImage ->
                                    objectMapper.convertValue(gifticonImage, GifticonQueryDto.Image.class)
                            )
                            .toList())
            .tags(
                    gifticon.getTags().stream()
                            .map(tag -> objectMapper.convertValue(tag, GifticonQueryDto.Tag.class)
                            )
                            .toList()
            ).sellerSummary(GifticonQueryDto.SellerSummary.builder()
                    .id(summary.getId())
                    .average(summary.getAverageRating())
                    .reviewCount(summary.getTotalCount())
                    .distribution(summary.getDistribution())
                    .build())
            .build();
  }
}
