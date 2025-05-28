package com.mkhwang.trader.query.category.application;

import com.mkhwang.trader.common.brand.domain.QBrand;
import com.mkhwang.trader.common.category.domain.Category;
import com.mkhwang.trader.common.category.infra.CategoryRepository;
import com.mkhwang.trader.common.config.GenericMapper;
import com.mkhwang.trader.query.config.QuerydslUtil;
import com.mkhwang.trader.common.dto.PaginationDto;
import com.mkhwang.trader.common.exception.ResourceNotFoundException;
import com.mkhwang.trader.common.gifticon.domain.QGifticon;
import com.mkhwang.trader.common.gifticon.domain.QGifticonImage;
import com.mkhwang.trader.common.gifticon.domain.QGifticonPrice;
import com.mkhwang.trader.common.user.domain.QUser;
import com.mkhwang.trader.query.category.presentation.dto.CategoryDto;
import com.mkhwang.trader.query.gifticon.application.dto.GifticonQueryDto;
import com.mkhwang.trader.query.gifticon.application.dto.QGifticonQueryDto_GifticonSummary;
import com.mkhwang.trader.query.gifticon.application.dto.QGifticonQueryDto_Brand;
import com.mkhwang.trader.query.gifticon.application.dto.QGifticonQueryDto_Image;
import com.mkhwang.trader.query.gifticon.application.dto.QGifticonQueryDto_Seller;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
  private final GenericMapper genericMapper;
  private final CategoryRepository categoryRepository;
  private final QuerydslUtil querydslUtil;
  private final JPAQueryFactory jpaQueryFactory;
  private final QGifticon qGifticon = QGifticon.gifticon;
  private final QGifticonPrice qGifticonPrice = QGifticonPrice.gifticonPrice;
  private final QBrand qBrand = QBrand.brand;
  private final QUser qUser = QUser.user;
  private final QGifticonImage qGifticonImage = QGifticonImage.gifticonImage;


  @Override
  @Transactional(readOnly = true)
  public List<CategoryDto.Category> getAllCategories(Integer level) {
    List<Category> all = categoryRepository.findAll();
    List<CategoryDto.Category> root = new ArrayList<>();

    for (Category category : all) {
      if (category.getLevel() == null || category.getLevel() == 1) {
        root.add(genericMapper.toDto(category, CategoryDto.Category.class));
      } else {
        root.stream()
                .filter(c -> c.getId().equals(category.getParent().getId()))
                .findFirst().ifPresent(parent ->
                        parent.getChildren().add(genericMapper.toDto(category, CategoryDto.Category.class)));
      }
    }

    if (level != null) {
      return root.stream().filter(
                      c -> c.getLevel() != null && c.getLevel().equals(level))
              .toList();
    }
    return root;
  }

  @Override
  @Transactional(readOnly = true)
  public CategoryDto.CategoryGifticon getCategoryGifticons(Long categoryId, Boolean includeSubcategories,
                                                           PaginationDto.PaginationRequest paginationRequest) {

    Category existCategory = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Category", categoryId));

    CategoryDto.Detail category = genericMapper.toDto(existCategory, CategoryDto.Detail.class);

    Pageable pageable = paginationRequest.toPageable();
    BooleanBuilder condition = buildSearchConditions(categoryId, includeSubcategories);

    long total = Optional.ofNullable(
            jpaQueryFactory.select(qGifticon.count())
                    .from(qGifticon).where(condition).fetchOne()

    ).orElse(0L);

    if (total == 0) {
      return CategoryDto.CategoryGifticon.builder()
              .category(category)
              .items(List.of())
              .pagination(PaginationDto.PaginationInfo.empty(pageable))
              .build();
    }

    List<GifticonQueryDto.GifticonSummary> items = jpaQueryFactory
            .select(
                    new QGifticonQueryDto_GifticonSummary(
                            qGifticon.id,
                            qGifticon.name,
                            qGifticon.description,
                            qGifticonPrice.basePrice,
                            qGifticonPrice.salePrice,
                            qGifticonPrice.currency,
                            new QGifticonQueryDto_Image(
                                    qGifticonImage.id,
                                    qGifticonImage.url,
                                    qGifticonImage.altText,
                                    qGifticonImage.isPrimary,
                                    qGifticonImage.displayOrder
                            ),
                            new QGifticonQueryDto_Brand(
                                    qBrand.id,
                                    qBrand.name
                            ),
                            new QGifticonQueryDto_Seller(
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


    return CategoryDto.CategoryGifticon.builder()
            .category(category)
            .items(items)
            .pagination(PaginationDto.PaginationInfo.builder()
                    .totalItems((int) total)
                    .totalPages(pageable.getOffset() == 0 ? 1 : (int) Math.ceil((double) total / pageable.getPageSize()))
                    .currentPage(pageable.getPageNumber()) // 0-based to 1-based
                    .perPage(pageable.getPageSize())
                    .build())
            .build();
  }

  private BooleanBuilder buildSearchConditions(Long categoryId, Boolean includeSubcategories) {
    BooleanBuilder builder = new BooleanBuilder();

    List<Long> categoryIds = new ArrayList<>();
    categoryIds.add(categoryId);

    if (Boolean.TRUE.equals(includeSubcategories)) {
      categoryIds.addAll(this.getAllSubcategories(categoryId));
    }

    builder.and(qGifticon.category.id.in(categoryIds));
    return builder;
  }

  private List<Long> getAllSubcategories(Long categoryId) {
    List<Category> subCategories = categoryRepository.findAll();
    return this.recursiveGetSubcategories(categoryId, subCategories);
  }

  private List<Long> recursiveGetSubcategories(Long categoryId, List<Category> allCategories) {
    List<Long> directChildren = allCategories.stream()
            .filter(category -> category.getParent() != null
                    && Objects.equals(category.getParent().getId(), categoryId))
            .map(Category::getId)
            .toList();

    return Stream.concat(
                    directChildren.stream(),
                    directChildren.stream()
                            .flatMap(childId -> recursiveGetSubcategories(childId, allCategories).stream())
            )
            .toList();
  }
}
