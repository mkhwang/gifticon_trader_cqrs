package com.mkhwang.trader.query.gifticon.application;

import com.mkhwang.trader.common.brand.domain.Brand;
import com.mkhwang.trader.common.brand.infra.BrandRepository;
import com.mkhwang.trader.common.category.domain.Category;
import com.mkhwang.trader.common.category.infra.CategoryRepository;
import com.mkhwang.trader.common.dto.PaginationDto;
import com.mkhwang.trader.common.exception.ResourceNotFoundException;
import com.mkhwang.trader.common.gifticon.domain.Gifticon;
import com.mkhwang.trader.common.gifticon.domain.GifticonPrice;
import com.mkhwang.trader.common.gifticon.domain.GifticonStatus;
import com.mkhwang.trader.common.gifticon.infra.GifticonRepository;
import com.mkhwang.trader.common.review.domain.Review;
import com.mkhwang.trader.common.review.infra.ReviewRepository;
import com.mkhwang.trader.common.user.domain.User;
import com.mkhwang.trader.common.user.infra.UserRepository;
import com.mkhwang.trader.query.gifticon.application.dto.GifticonQueryDto;
import com.mkhwang.trader.query.gifticon.application.query.GifticonQuery;
import com.mkhwang.trader.query.gifticon.presentation.dto.GifticonListResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import support.AbstractIntegrationTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
class GifticonJpaQueryServiceTest extends AbstractIntegrationTest {
  @Autowired
  private GifticonJpaQueryService gifticonJpaQueryService;
  @Autowired
  private ReviewRepository reviewRepository;
  @Autowired
  private GifticonRepository gifticonRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private CategoryRepository categoryRepository;
  @Autowired
  private BrandRepository brandRepository;


  Gifticon gifticon1;
  Category category1;
  Category category2;
  User seller;
  Brand brand;

  @BeforeEach
  void setUp() {
    category1 = Category.builder().level(1).name("name1").slug("slug1").build();
    category2 = Category.builder().level(1).name("name2").slug("slug2").build();
    categoryRepository.saveAll(List.of(category1, category2));
    LocalDate dueDate = LocalDate.now().plusDays(10);
    seller = User.of("user1", "nickname1", "http://image.profile.com");
    User buyer = User.of("user2", "nickname2", "http://image.profile.com");
    userRepository.save(seller);
    userRepository.save(buyer);
    brand = Brand.builder().name("brand").slug("slug").description("des").build();
    brandRepository.save(brand);

    GifticonPrice price1 = GifticonPrice.builder().basePrice(BigDecimal.ZERO).salePrice(BigDecimal.ZERO).build();
    GifticonPrice price2 = GifticonPrice.builder().basePrice(BigDecimal.valueOf(10000L)).salePrice(BigDecimal.valueOf(1000L)).build();
    GifticonPrice price3 = GifticonPrice.builder().basePrice(BigDecimal.valueOf(30000L)).salePrice(BigDecimal.valueOf(3000L)).build();

    gifticon1 = Gifticon.builder()
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

    gifticon1.buy(buyer);

    gifticonRepository.saveAll(List.of(gifticon1, gifticon2, gifticon3));

    Review review1 = Review.builder()
            .reviewer(buyer)
            .user(seller)
            .title("title")
            .rating(5)
            .content("good")
            .gifticon(gifticon2)
            .build();
    reviewRepository.save(review1);
  }

  @DisplayName("기프티콘이 없는경우 ResourceNotFoundException 발생")
  @Test
  void getGifticon_if_gifticon_not_exists_throw_ResourceNotFoundException() {
    // given
    GifticonQuery.GetGifticon query = GifticonQuery.GetGifticon.builder().gifticonId(99999L).build();

    // when & then
    assertThrows(ResourceNotFoundException.class, () -> gifticonJpaQueryService.getGifticon(query));
  }

  @DisplayName("기프티콘이 있는 경우 Gifticon Dto return")
  @Test
  void getGifticon_if_gifticon_exists_should_return_gifticon_dto() {
    // given
    GifticonQuery.GetGifticon query = GifticonQuery.GetGifticon.builder().gifticonId(gifticon1.getId()).build();

    // when
    GifticonQueryDto.Gifticon gifticon = gifticonJpaQueryService.getGifticon(query);

    // then
    assertNotNull(gifticon);
    assertEquals(gifticon1.getId(), gifticon.getId());
  }

  @DisplayName("기프티콘이 없는경우 items 0")
  @Test
  void getGifticons_if_gifticoin_not_exists_return_items_emtpy() {
    // given
    GifticonQuery.ListGifticons query = GifticonQuery.ListGifticons.builder()
            .search("99999")
            .pagination(
                    PaginationDto.PaginationRequest.builder()
                            .page(1)
                            .size(10)
                            .sort("createdAt:desc")
                            .build()
            )
            .build();

    // when
    GifticonListResponse gifticons = gifticonJpaQueryService.getGifticons(query);

    // then
    assertNotNull(gifticons);
    assertNotNull(gifticons.getItems());
    assertNotNull(gifticons.getPagination());
    assertEquals(0, gifticons.getItems().size());
    assertEquals(0, gifticons.getPagination().getTotalItems());
  }

  @DisplayName("기프티콘이 있는 경우")
  @Test
  void getGifticons_if_gifticoin_exists_return_items() {
    // given
    GifticonQuery.ListGifticons query = GifticonQuery.ListGifticons.builder()
            .search("name")
            .pagination(
                    PaginationDto.PaginationRequest.builder()
                            .page(1)
                            .size(10)
                            .sort("createdAt:desc")
                            .build()
            )
            .build();

    // when
    GifticonListResponse gifticons = gifticonJpaQueryService.getGifticons(query);

    // then
    assertNotNull(gifticons);
    assertNotNull(gifticons.getItems());
    assertNotNull(gifticons.getPagination());
    assertEquals(3, gifticons.getItems().size());
    assertEquals(3, gifticons.getPagination().getTotalItems());
  }

  @DisplayName("기프티콘이 상태 검색 테스트")
  @Test
  void getGifticons_if_gifticoin_status_exists_return_items() {
    // given
    GifticonQuery.ListGifticons query = GifticonQuery.ListGifticons.builder()
            .status(GifticonStatus.SOLD_OUT.toString())
            .pagination(
                    PaginationDto.PaginationRequest.builder()
                            .page(1)
                            .size(10)
                            .sort("createdAt:desc")
                            .build()
            )
            .build();

    // when
    GifticonListResponse gifticons = gifticonJpaQueryService.getGifticons(query);

    // then
    assertNotNull(gifticons);
    assertNotNull(gifticons.getItems());
    assertNotNull(gifticons.getPagination());
    assertEquals(1, gifticons.getItems().size());
    assertEquals(1, gifticons.getPagination().getTotalItems());
  }

  @DisplayName("기프티콘이 가격 검색 테스트")
  @Test
  void getGifticons_if_gifticoin_price_search_return_items() {
    // given
    GifticonQuery.ListGifticons query = GifticonQuery.ListGifticons.builder()
            .minPrice(BigDecimal.valueOf(500L))
            .maxPrice(BigDecimal.valueOf(2500L))
            .pagination(
                    PaginationDto.PaginationRequest.builder()
                            .page(1)
                            .size(10)
                            .sort("createdAt:desc")
                            .build()
            )
            .build();

    // when
    GifticonListResponse gifticons = gifticonJpaQueryService.getGifticons(query);

    // then
    assertNotNull(gifticons);
    assertNotNull(gifticons.getItems());
    assertNotNull(gifticons.getPagination());
    assertEquals(1, gifticons.getItems().size());
    assertEquals(1, gifticons.getPagination().getTotalItems());
  }

  @DisplayName("기프티콘이 카테고리 검색 테스트")
  @Test
  void getGifticons_if_gifticoin_category_search_return_items() {
    // given
    GifticonQuery.ListGifticons query = GifticonQuery.ListGifticons.builder()
            .category(List.of(category2.getId()))
            .pagination(
                    PaginationDto.PaginationRequest.builder()
                            .page(1)
                            .size(10)
                            .sort("createdAt:desc")
                            .build()
            )
            .build();

    // when
    GifticonListResponse gifticons = gifticonJpaQueryService.getGifticons(query);

    // then
    assertNotNull(gifticons);
    assertNotNull(gifticons.getItems());
    assertNotNull(gifticons.getPagination());
    assertEquals(1, gifticons.getItems().size());
    assertEquals(1, gifticons.getPagination().getTotalItems());
  }


  @DisplayName("기프티콘이 판매자 검색 테스트")
  @Test
  void getGifticons_if_gifticoin_seller_search_return_items() {
    // given
    GifticonQuery.ListGifticons query = GifticonQuery.ListGifticons.builder()
            .seller(this.seller.getId())
            .pagination(
                    PaginationDto.PaginationRequest.builder()
                            .page(1)
                            .size(10)
                            .sort("createdAt:desc")
                            .build()
            )
            .build();

    // when
    GifticonListResponse gifticons = gifticonJpaQueryService.getGifticons(query);

    // then
    assertNotNull(gifticons);
    assertNotNull(gifticons.getItems());
    assertNotNull(gifticons.getPagination());
    assertEquals(3, gifticons.getItems().size());
    assertEquals(3, gifticons.getPagination().getTotalItems());
  }


  @DisplayName("기프티콘이 브랜드 검색 테스트")
  @Test
  void getGifticons_if_gifticoin_brand_search_return_items() {
    // given
    GifticonQuery.ListGifticons query = GifticonQuery.ListGifticons.builder()
            .brand(this.brand.getId())
            .pagination(
                    PaginationDto.PaginationRequest.builder()
                            .page(1)
                            .size(10)
                            .sort("createdAt:desc")
                            .build()
            )
            .build();

    // when
    GifticonListResponse gifticons = gifticonJpaQueryService.getGifticons(query);

    // then
    assertNotNull(gifticons);
    assertNotNull(gifticons.getItems());
    assertNotNull(gifticons.getPagination());
    assertEquals(3, gifticons.getItems().size());
    assertEquals(3, gifticons.getPagination().getTotalItems());
  }


  @DisplayName("기프티콘이 등록일 검색 테스트")
  @Test
  void getGifticons_if_gifticoin_created_at_search_return_items() {
    // given
    LocalDate today = LocalDate.now();
    GifticonQuery.ListGifticons query = GifticonQuery.ListGifticons.builder()
            .createdFrom(today.minusDays(1))
            .createdTo(today.plusDays(1))
            .pagination(
                    PaginationDto.PaginationRequest.builder()
                            .page(1)
                            .size(10)
                            .sort("createdAt:desc")
                            .build()
            )
            .build();

    // when
    GifticonListResponse gifticons = gifticonJpaQueryService.getGifticons(query);

    // then
    assertNotNull(gifticons);
    assertNotNull(gifticons.getItems());
    assertNotNull(gifticons.getPagination());
    assertEquals(3, gifticons.getItems().size());
    assertEquals(3, gifticons.getPagination().getTotalItems());
  }
}