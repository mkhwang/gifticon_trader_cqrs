package com.mkhwang.trader.query.category.application;

import com.mkhwang.trader.common.brand.domain.Brand;
import com.mkhwang.trader.common.brand.infra.BrandRepository;
import com.mkhwang.trader.common.category.domain.Category;
import com.mkhwang.trader.common.category.infra.CategoryRepository;
import com.mkhwang.trader.common.config.GenericMapper;
import com.mkhwang.trader.common.config.ModelMapperConfig;
import com.mkhwang.trader.common.dto.PaginationDto;
import com.mkhwang.trader.common.exception.ResourceNotFoundException;
import com.mkhwang.trader.common.gifticon.domain.Gifticon;
import com.mkhwang.trader.common.gifticon.domain.GifticonPrice;
import com.mkhwang.trader.common.gifticon.domain.GifticonStatus;
import com.mkhwang.trader.common.gifticon.infra.GifticonRepository;
import com.mkhwang.trader.common.user.domain.User;
import com.mkhwang.trader.common.user.infra.UserRepository;
import com.mkhwang.trader.query.category.presentation.dto.CategoryDto;
import com.mkhwang.trader.query.config.QuerydslUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import support.TestJpaConfig;
import support.TestQueryDslConfig;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ActiveProfiles("test")
@DataJpaTest
@Import({CategoryServiceImpl.class, TestJpaConfig.class, TestQueryDslConfig.class, ModelMapperConfig.class,
        GenericMapper.class, QuerydslUtil.class})
class CategoryServiceImplJpaTest {
  @Autowired
  GenericMapper genericMapper;
  @Autowired
  CategoryRepository categoryRepository;
  @Autowired
  QuerydslUtil querydslUtil;
  @Autowired
  private CategoryServiceImpl categoryService;
  @Autowired
  private GifticonRepository gifticonRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private BrandRepository brandRepository;

  @DisplayName("존재하지 않는 카테고리 ID로 조회 시 ResourceNotFoundException이 발생")
  @Test
  void getCategoryGifticons_shouldThrow_whenCategoryNotFound() {
    // given
    Long categoryId = 9999L;
    Boolean includeSubcategories = true;
    PaginationDto.PaginationRequest paginationRequest = new PaginationDto.PaginationRequest(1, 10, "createdAt:desc");

    // when & then
    assertThrows(
            ResourceNotFoundException.class, () -> {
              categoryService.getCategoryGifticons(categoryId, includeSubcategories, paginationRequest);
            }
    );
  }

  @DisplayName("카테고리에 해당하는 기프티콘이 없을 시에, 빈 목록 return")
  @Test
  void getCategoryGifticons_shouldReturn_empty_whenTotal_0() {
    // given
    Category category = Category.builder().level(1).name("name").slug("slug").build();
    categoryRepository.save(category);

    Long categoryId = category.getId();
    Boolean includeSubcategories = true;
    PaginationDto.PaginationRequest paginationRequest = new PaginationDto.PaginationRequest(1, 10, "createdAt:desc");

    // when
    CategoryDto.CategoryGifticon categoryGifticons = categoryService.getCategoryGifticons(categoryId, includeSubcategories, paginationRequest);

    // then
    assertNotNull(categoryGifticons);
    assertNotNull(categoryGifticons.getItems());
    assertEquals(0, categoryGifticons.getItems().size());
    assertEquals(0, categoryGifticons.getPagination().getTotalItems());
  }

  @DisplayName("카테고리에 해당하는 기프티콘수가 0보다 클때, 기프티콘 목록을 return")
  @Test
  void getCategoryGifticons_shouldReturn_gifticons_whenTotal_gt_0() {
    // given
    Category category1 = Category.builder().level(1).name("name1").slug("slug1").build();
    Category category2 = Category.builder().level(1).name("name2").slug("slug2").build();
    categoryRepository.saveAll(List.of(category1, category2));
    LocalDate dueDate = LocalDate.now().plusDays(10);
    User seller = User.of("user", "nickname", "http://image.profile.com");
    userRepository.save(seller);
    Brand brand = Brand.builder().name("brand").slug("slug").description("des").build();
    brandRepository.save(brand);

    GifticonPrice price1 = GifticonPrice.builder().basePrice(BigDecimal.ZERO).salePrice(BigDecimal.ZERO).build();
    GifticonPrice price2 = GifticonPrice.builder().basePrice(BigDecimal.ZERO).salePrice(BigDecimal.ZERO).build();
    GifticonPrice price3 = GifticonPrice.builder().basePrice(BigDecimal.ZERO).salePrice(BigDecimal.ZERO).build();

    Gifticon gifticon1 = Gifticon.builder()
            .name("name")
            .slug("slug")
            .dueDate(dueDate)
            .seller(seller)
            .brand(brand)
            .status(GifticonStatus.ON_SALE)
            .price(price1)
            .category(category1)
            .build();
    Gifticon gifticon2 = Gifticon.builder()
            .name("name")
            .slug("slug")
            .dueDate(dueDate)
            .seller(seller)
            .brand(brand)
            .status(GifticonStatus.ON_SALE)
            .category(category1)
            .price(price2)
            .build();
    Gifticon gifticon3 = Gifticon.builder()
            .name("name")
            .slug("slug")
            .dueDate(dueDate)
            .seller(seller)
            .brand(brand)
            .status(GifticonStatus.ON_SALE)
            .category(category2)
            .price(price3)
            .build();

    price1.setGifticon(gifticon1);
    price2.setGifticon(gifticon2);
    price3.setGifticon(gifticon3);

    gifticonRepository.saveAll(List.of(gifticon1, gifticon2, gifticon3));

    Long categoryId = category1.getId();
    Boolean includeSubcategories = true;
    PaginationDto.PaginationRequest paginationRequest = new PaginationDto.PaginationRequest(1, 10, "createdAt:desc");

    // when
    CategoryDto.CategoryGifticon categoryGifticons = categoryService.getCategoryGifticons(categoryId, includeSubcategories, paginationRequest);

    // then
    assertNotNull(categoryGifticons);
    assertNotNull(categoryGifticons.getItems());
    assertEquals(2, categoryGifticons.getItems().size());
    assertEquals(2, categoryGifticons.getPagination().getTotalItems());
  }
}