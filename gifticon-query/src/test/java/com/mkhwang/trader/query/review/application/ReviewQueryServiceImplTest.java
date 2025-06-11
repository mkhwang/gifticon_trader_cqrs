package com.mkhwang.trader.query.review.application;

import com.mkhwang.trader.common.brand.domain.Brand;
import com.mkhwang.trader.common.brand.infra.BrandRepository;
import com.mkhwang.trader.common.category.domain.Category;
import com.mkhwang.trader.common.category.infra.CategoryRepository;
import com.mkhwang.trader.common.dto.PaginationDto;
import com.mkhwang.trader.common.gifticon.domain.Gifticon;
import com.mkhwang.trader.common.gifticon.domain.GifticonPrice;
import com.mkhwang.trader.common.gifticon.domain.GifticonStatus;
import com.mkhwang.trader.common.gifticon.infra.GifticonRepository;
import com.mkhwang.trader.common.review.domain.Review;
import com.mkhwang.trader.common.review.infra.ReviewRepository;
import com.mkhwang.trader.common.user.domain.User;
import com.mkhwang.trader.common.user.infra.UserRepository;
import com.mkhwang.trader.query.review.presentation.dto.ReviewDto;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
class ReviewQueryServiceImplTest extends AbstractIntegrationTest {
  @Autowired
  private ReviewQueryServiceImpl reviewQueryService;
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

  @BeforeEach
  void setUp() {
    Category category1 = Category.builder().level(1).name("name1").slug("slug1").build();
    Category category2 = Category.builder().level(1).name("name2").slug("slug2").build();
    categoryRepository.saveAll(List.of(category1, category2));
    LocalDate dueDate = LocalDate.now().plusDays(10);
    User seller = User.of("user1", "nickname1", "http://image.profile.com");
    User buyer = User.of("user2", "nickname2", "http://image.profile.com");
    userRepository.save(seller);
    userRepository.save(buyer);
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

  @DisplayName("리뷰검색 결과가 없을 때")
  @Test
  void test_searchReviews_no_results() {
    // given
    String keyword = "name2";
    PaginationDto.PaginationRequest page = PaginationDto.PaginationRequest.builder().page(1).size(10).sort("createdAt:desc").build();


    // when
    ReviewDto.ReviewPage reviewPage = reviewQueryService.searchReviews(keyword, page);

    // then
    assertNotNull(reviewPage);
    assertNotNull(reviewPage.getItems());
    assertEquals(0, reviewPage.getItems().size());
    assertEquals(0, reviewPage.getPagination().getTotalItems());
  }

  @DisplayName("리뷰검색 결과가 있을 때")
  @Test
  void test_searchReviews_have_results() {
    // given
    String keyword = "name";
    PaginationDto.PaginationRequest page = PaginationDto.PaginationRequest.builder().page(1).size(10).sort("createdAt:desc").build();


    // when
    ReviewDto.ReviewPage reviewPage = reviewQueryService.searchReviews(keyword, page);

    // then
    assertNotNull(reviewPage);
    assertNotNull(reviewPage.getItems());
    assertEquals(1, reviewPage.getItems().size());
    assertEquals(1, reviewPage.getPagination().getTotalItems());
  }
}