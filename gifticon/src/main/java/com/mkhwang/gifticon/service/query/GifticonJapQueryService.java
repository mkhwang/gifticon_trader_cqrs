package com.mkhwang.gifticon.service.query;

import com.mkhwang.gifticon.controller.dto.GifticonListResponse;
import com.mkhwang.gifticon.exception.ResourceNotFoundException;
import com.mkhwang.gifticon.repository.GifticonRepository;
import com.mkhwang.gifticon.service.dto.ReviewDto;
import com.mkhwang.gifticon.service.entity.Gifticon;
import com.mkhwang.gifticon.service.entity.QReview;
import com.mkhwang.gifticon.service.gifticon.GifticonDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class GifticonJapQueryService implements GifticonQueryHandler {

  private final GifticonRepository gifticonRepository;
  private final JPAQueryFactory jpaQueryFactory;
  private final QReview qReview = QReview.review;

  @Override
  @Transactional(readOnly = true)
  public GifticonDto.Gifticon getGifticon(GifticonQuery.GetGifticon query) {
    Gifticon gifticon = gifticonRepository.findById(query.getGifticonId()).orElseThrow(
            () -> new ResourceNotFoundException("Gifticon", query.getGifticonId())
    );

    return GifticonDto.Gifticon.builder()
            .id(query.getGifticonId())
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
            ).sellerSummary(this.getSellerSummary(gifticon.getSeller().getId()))
            .build();
  }

  @Override
  @Transactional(readOnly = true)
  public GifticonListResponse getGifticons(GifticonQuery.ListGifticons query) {
    return null;
  }

  private GifticonDto.SellerSummary getSellerSummary(Long userId) {
    ReviewDto.FlatRatingStatDto flatRating = jpaQueryFactory
            .select(
                    Projections.constructor(ReviewDto.FlatRatingStatDto.class,
                            qReview.rating.avg(),
                            qReview.gifticon.count().intValue().as("count"),
                            Expressions.numberTemplate(Integer.class, "SUM(CASE WHEN {0} = 1 THEN 1 ELSE 0 END)", qReview.rating),
                            Expressions.numberTemplate(Integer.class, "SUM(CASE WHEN {0} = 2 THEN 1 ELSE 0 END)", qReview.rating),
                            Expressions.numberTemplate(Integer.class, "SUM(CASE WHEN {0} = 3 THEN 1 ELSE 0 END)", qReview.rating),
                            Expressions.numberTemplate(Integer.class, "SUM(CASE WHEN {0} = 4 THEN 1 ELSE 0 END)", qReview.rating),
                            Expressions.numberTemplate(Integer.class, "SUM(CASE WHEN {0} = 5 THEN 1 ELSE 0 END)", qReview.rating)
                    )

            )
            .from(qReview)
            .where(qReview.user.id.eq(userId))
            .groupBy(qReview.user)
            .fetchOne();
    if (flatRating == null) {
      return null;
    }
    return GifticonDto.SellerSummary.builder()
            .id(userId)
            .average(flatRating.getAverage())
            .reviewCount(flatRating.getCount())
            .distribution(Map.of(
                    1, flatRating.getRating1(),
                    2, flatRating.getRating2(),
                    3, flatRating.getRating3(),
                    4, flatRating.getRating4(),
                    5, flatRating.getRating5()
            )).build();

  }
}
