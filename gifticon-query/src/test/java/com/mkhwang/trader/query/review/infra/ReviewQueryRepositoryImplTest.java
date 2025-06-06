package com.mkhwang.trader.query.review.infra;

import com.mkhwang.trader.common.brand.domain.Brand;
import com.mkhwang.trader.common.brand.infra.BrandRepository;
import com.mkhwang.trader.common.category.domain.Category;
import com.mkhwang.trader.common.category.infra.CategoryRepository;
import com.mkhwang.trader.common.gifticon.domain.Gifticon;
import com.mkhwang.trader.common.gifticon.domain.GifticonStatus;
import com.mkhwang.trader.common.gifticon.infra.GifticonRepository;
import com.mkhwang.trader.common.review.domain.Review;
import com.mkhwang.trader.common.review.infra.ReviewRepository;
import com.mkhwang.trader.common.user.domain.User;
import com.mkhwang.trader.common.user.infra.UserRepository;
import com.mkhwang.trader.query.config.TestJpaConfig;
import com.mkhwang.trader.query.config.TestQueryDslConfig;
import com.mkhwang.trader.query.review.presentation.dto.ReviewDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@Transactional
@DataJpaTest
@Import({ReviewQueryRepositoryImpl.class, TestJpaConfig.class, TestQueryDslConfig.class})
class ReviewQueryRepositoryImplTest {

  @Autowired
  private ReviewQueryRepositoryImpl reviewQueryRepository;

  @Autowired
  private TestEntityManager em;

  @Autowired
  private ReviewRepository reviewRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private GifticonRepository gifticonRepository;

  @Autowired
  private BrandRepository brandRepository;

  @Autowired
  private CategoryRepository categoryRepository;


  @DisplayName("등록된 리뷰가 없을 때 테스트")
  @Test
  void getUserReviewSummary_정상동작() {
    // given
    User user = User.of(
            "testUser",
            "testUser",
            "https://example.com/profile.jpg");
    em.persist(user);

    // when
    ReviewDto.ReviewSummary userReviewSummary = reviewQueryRepository.getUserReviewSummary(user.getId());

    // then
    assertNotNull(userReviewSummary);
    assertEquals(0d, userReviewSummary.getAverageRating());
    assertEquals(0, userReviewSummary.getTotalCount());
    assertEquals(Map.of(), userReviewSummary.getDistribution());

  }

  @DisplayName("등록된 리뷰가 있을 때 테스트")
  @Test
  void getUserReviewSummary_리뷰가_있을때() {
    // given
    User user1 = User.of("user1", "user1", "http://image.user.com");
    User user2 = User.of("user2", "user2", "http://image.user.com");
    userRepository.save(user1);
    userRepository.save(user2);


    Category category = Category.builder().name("1차 카테고리").slug("1").level(1).build();
    categoryRepository.save(category);

    Brand brand = Brand.builder().name("brand").slug("b").description("1").build();
    brandRepository.save(brand);

    LocalDate now = LocalDate.now();
    Gifticon gifticon1 = Gifticon.builder().name("gifticon1").category(category)
            .brand(brand).seller(user2).buyer(user1).dueDate(now).status(GifticonStatus.SOLD_OUT).build();
    Gifticon gifticon2 = Gifticon.builder().name("gifticon2").category(category)
            .brand(brand).seller(user2).buyer(user1).dueDate(now).status(GifticonStatus.SOLD_OUT).build();

    gifticonRepository.save(gifticon1);
    gifticonRepository.save(gifticon2);

    Review review1 = Review.builder().user(user1).reviewer(user2).content("리뷰 1")
            .gifticon(gifticon1).rating(1).title("리뷰 1").build();
    Review review2 = Review.builder().user(user1).reviewer(user2).content("리뷰 2")
            .gifticon(gifticon2).rating(2).title("리뷰 2").build();
    reviewRepository.saveAll(List.of(review1, review2));

    // when
    ReviewDto.ReviewSummary userReviewSummary = reviewQueryRepository.getUserReviewSummary(user1.getId());

    // then
    assertNotNull(userReviewSummary);
    assertEquals(1.5d, userReviewSummary.getAverageRating());
    assertEquals(2, userReviewSummary.getTotalCount());
    assertEquals(Map.of(1, 1, 2, 1, 3, 0, 4, 0, 5, 0), userReviewSummary.getDistribution());
  }

}