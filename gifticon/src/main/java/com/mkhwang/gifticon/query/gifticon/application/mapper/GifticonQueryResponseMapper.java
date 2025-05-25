package com.mkhwang.gifticon.query.gifticon.application.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkhwang.gifticon.command.gifticon.presentation.dto.GifticonDto;
import com.mkhwang.gifticon.command.gifticon.domain.Gifticon;
import com.mkhwang.gifticon.query.gifticon.domain.GifticonDocument;
import com.mkhwang.gifticon.query.gifticon.domain.UserRatingSummary;
import com.mkhwang.gifticon.query.review.presentation.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GifticonQueryResponseMapper {
  private final ObjectMapper objectMapper;

  public GifticonDto.Gifticon toGifticon(Gifticon gifticon, ReviewDto.ReviewSummary summary) {
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
                            .toList()
            ).sellerSummary(
                    GifticonDto.SellerSummary.builder()
                            .average(summary.getAverageRating())
                            .reviewCount(summary.getTotalCount())
                            .distribution(summary.getDistribution())
                            .build()
            )
            .build();
  }


  public GifticonDto.Gifticon toGifticon(GifticonDocument gifticon, UserRatingSummary summary) {
    return GifticonDto.Gifticon.builder()
            .id(gifticon.getId())
            .name(gifticon.getName())
            .slug(gifticon.getSlug())
            .description(gifticon.getDescription())
            .brand(objectMapper.convertValue(gifticon.getBrand(), GifticonDto.Brand.class))
            .seller(objectMapper.convertValue(gifticon.getSeller(), GifticonDto.Seller.class))
            .status(gifticon.getStatus().toString())
            .createdAt(gifticon.getCreatedAt())
            .updatedAt(gifticon.getUpdatedAt())
            .price(objectMapper.convertValue(gifticon.getPrice(), GifticonDto.Price.class))
            .category(objectMapper.convertValue(gifticon.getCategory(), GifticonDto.Category.class))
            .images(
                    gifticon.getImages().stream()
                            .map(gifticonImage ->
                                    objectMapper.convertValue(gifticonImage, GifticonDto.Image.class)
                            )
                            .toList())
            .tags(
                    gifticon.getTags().stream()
                            .map(tag -> objectMapper.convertValue(tag, GifticonDto.Tag.class)
                            )
                            .toList()
            ).sellerSummary(GifticonDto.SellerSummary.builder()
                    .id(summary.getId())
                    .average(summary.getAverageRating())
                    .reviewCount(summary.getTotalCount())
                    .distribution(summary.getDistribution())
                    .build())
            .build();
  }
}
