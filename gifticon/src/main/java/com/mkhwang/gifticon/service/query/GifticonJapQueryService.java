package com.mkhwang.gifticon.service.query;

import com.mkhwang.gifticon.config.QuerydslUtil;
import com.mkhwang.gifticon.controller.dto.GifticonListResponse;
import com.mkhwang.gifticon.exception.ResourceNotFoundException;
import com.mkhwang.gifticon.repository.GifticonRepository;
import com.mkhwang.gifticon.service.dto.PaginationDto;
import com.mkhwang.gifticon.service.dto.ReviewDto;
import com.mkhwang.gifticon.service.entity.*;
import com.mkhwang.gifticon.service.gifticon.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GifticonJapQueryService implements GifticonQueryHandler {

  private final GifticonRepository gifticonRepository;
  private final JPAQueryFactory jpaQueryFactory;
  private final QGifticon qGifticon = QGifticon.gifticon;
  private final QReview qReview = QReview.review;
  private final QBrand qBrand = QBrand.brand;
  private final QCategory qCategory = QCategory.category;
  private final QGifticonPrice qGifticonPrice = QGifticonPrice.gifticonPrice;
  private final QTag qTag = QTag.tag;
  private final QuerydslUtil querydslUtil;
  private final QUser qUser = QUser.user;
  private final QGifticonImage qGifticonImage = QGifticonImage.gifticonImage;


  @Override
  @Transactional(readOnly = true)
  public GifticonDto.Gifticon getGifticon(GifticonQuery.GetGifticon query) {
    Gifticon gifticon = gifticonRepository.findById(query.getGifticonId()).orElseThrow(
            () -> new ResourceNotFoundException("Gifticon", query.getGifticonId())
    );

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
            ).sellerSummary(this.getSellerSummary(gifticon.getSeller().getId()))
            .build();
  }

  @Override
  @Transactional(readOnly = true)
  public GifticonListResponse getGifticons(GifticonQuery.ListGifticons query) {
    Pageable pageable = query.getPagination().toPageable();
    BooleanBuilder condition = this.generateCondition(query);

    long total = Optional.ofNullable(
                    jpaQueryFactory.select(qGifticon.count())
                            .from(qGifticon)
                            .where(condition)
                            .fetchOne())
            .orElse(0L);

    if (total == 0) {
      return GifticonListResponse.builder()
              .items(List.of())
              .pagination(PaginationDto.PaginationInfo.empty(pageable))
              .build();
    }

    List<GifticonDto.GifticonSummary> items = jpaQueryFactory
            .select(
                    new QGifticonDto_GifticonSummary(
                            qGifticon.id,
                            qGifticon.name,
                            qGifticon.description,
                            qGifticonPrice.basePrice,
                            qGifticonPrice.salePrice,
                            qGifticonPrice.currency,
                            new QGifticonDto_Image(
                                    qGifticonImage.id,
                                    qGifticonImage.url,
                                    qGifticonImage.altText,
                                    qGifticonImage.isPrimary,
                                    qGifticonImage.displayOrder
                            ),
                            new QGifticonDto_Brand(
                                    qBrand.id,
                                    qBrand.name
                            ),
                            new QGifticonDto_Seller(
                                    qUser.id,
                                    qUser.nickname
                            ),
                            qGifticon.status.stringValue(),
                            qGifticon.createdAt

                    )
            )
            .from(qGifticon)
            .join(qGifticon.brand, qBrand)
            .join(qGifticon.price, qGifticonPrice)
            .join(qGifticon.seller, qUser)
            .leftJoin(qGifticonImage)
            .on(qGifticonImage.gifticon.id.eq(qGifticon.id).and(qGifticonImage.isPrimary.eq(true)))
            .where(condition)
            .orderBy(querydslUtil.getOrderSpecifiers(pageable, qGifticon.getType(), qGifticon.getMetadata().getName()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    return GifticonListResponse.builder()
            .items(items)
            .pagination(
                    PaginationDto.PaginationInfo.builder()
                            .totalItems((int) total)
                            .totalPages((int) Math.ceil((double) total / pageable.getPageSize()))
                            .currentPage(pageable.getPageNumber() + 1) // 0-based to 1-based
                            .perPage(pageable.getPageSize())
                            .build()
            )
            .build();
  }

  private BooleanBuilder generateCondition(GifticonQuery.ListGifticons query) {
    BooleanBuilder condition = new BooleanBuilder();

    // 검색 필드: 상품명, 상품 전체 설명, 브랜드명, 태그명, 카테고리명
    if (StringUtils.hasText(query.getSearch())) {
      QGifticon gifticonSub = new QGifticon("gifticonSub");

      condition.and(qGifticon.name.containsIgnoreCase(query.getSearch())
              .or(qGifticon.description.containsIgnoreCase(query.getSearch())
                      .or(qBrand.name.containsIgnoreCase(query.getSearch()))
                      .or(JPAExpressions
                              .selectOne()
                              .from(gifticonSub)
                              .join(gifticonSub.tags, qTag)
                              .where(
                                      gifticonSub.id.eq(qGifticon.id),
                                      qTag.name.eq(query.getSearch())
                              )
                              .exists()
                      )
                      .or(JPAExpressions.selectOne()
                              .from(qCategory)
                              .where(qGifticon.category.eq(qCategory)
                                      .and(qCategory.name.containsIgnoreCase(query.getSearch()))
                              ).exists()
                      )
              )
      );

    }
    if (query.getStatus() != null) {
      condition.and(qGifticon.status.stringValue().eq(query.getStatus()));
    }
    if (query.getMinPrice() != null) {

      condition.and(
              JPAExpressions.selectOne()
                      .from(qGifticonPrice)
                      .where(qGifticonPrice.gifticon.eq(qGifticon)
                              .and(qGifticonPrice.salePrice.goe(query.getMinPrice())))
                      .exists()
      );
    }
    if (query.getMaxPrice() != null) {
      condition.and(
              JPAExpressions.selectOne()
                      .from(qGifticonPrice)
                      .where(qGifticonPrice.gifticon.eq(qGifticon)
                              .and(qGifticonPrice.salePrice.loe(query.getMaxPrice())))
                      .exists()
      );
    }
    if (query.getCategory() != null && !query.getCategory().isEmpty()) {
      condition.and(qGifticon.category.id.in(query.getCategory()));
    }
    if (query.getSeller() != null) {
      condition.and(qGifticon.seller.id.eq(query.getSeller()));
    }
    if (query.getBrand() != null) {
      condition.and(qGifticon.brand.id.eq(query.getBrand()));
    }

    if (query.getCreatedFrom() != null && query.getCreatedTo() != null) {
      condition.and(qGifticon.createdAt
              .between(query.getCreatedFrom().atStartOfDay(), query.getCreatedTo().atStartOfDay()));
    }


    return condition;
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
