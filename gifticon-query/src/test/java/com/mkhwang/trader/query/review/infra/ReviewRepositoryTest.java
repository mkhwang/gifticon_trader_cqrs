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
import support.TestJpaConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Transactional
@DataJpaTest
@Import(TestJpaConfig.class)
public class ReviewRepositoryTest {
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

  @DisplayName("사용자 기준으로 리뷰 찾기")
  @Test
  void findByUser() {
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
            .gifticon(gifticon2).rating(1).title("리뷰 2").build();
    reviewRepository.saveAll(List.of(review1, review2));

    Pageable pageable = PageRequest.of(0, 10);

    // when
    Page<Review> result = reviewRepository.findByUser(user1, pageable);

    // then
    assertThat(result.getTotalElements()).isEqualTo(2);
    assertThat(result.getContent()).extracting(Review::getContent)
            .containsExactlyInAnyOrder("리뷰 1", "리뷰 2");
  }
}
