package com.mkhwang.trader.query.gifticon.application.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkhwang.trader.common.brand.domain.Brand;
import com.mkhwang.trader.common.category.domain.Category;
import com.mkhwang.trader.common.gifticon.domain.Gifticon;
import com.mkhwang.trader.common.gifticon.domain.GifticonPrice;
import com.mkhwang.trader.common.gifticon.domain.GifticonStatus;
import com.mkhwang.trader.common.tag.domain.Tag;
import com.mkhwang.trader.common.user.domain.User;
import com.mkhwang.trader.query.gifticon.application.dto.GifticonQueryDto;
import com.mkhwang.trader.query.gifticon.domain.GifticonDocument;
import com.mkhwang.trader.query.gifticon.domain.UserRatingSummary;
import com.mkhwang.trader.query.review.presentation.dto.ReviewDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GifticonQueryResponseMapperTest {
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final GifticonQueryResponseMapper mapper = new GifticonQueryResponseMapper(objectMapper);

  @DisplayName("GifticonQuery response 변환 테스트")
  @Test
  void toGifticon() throws NoSuchFieldException, IllegalAccessException {
    // given
    Gifticon gifticon = new Gifticon();
    gifticon.setId(1L);
    gifticon.setName("test");
    gifticon.setDescription("description");
    gifticon.setStatus(GifticonStatus.ON_SALE);
    gifticon.setPrice(GifticonPrice.builder().basePrice(BigDecimal.ZERO).salePrice(BigDecimal.ZERO).build());
    gifticon.setCategory(Category.builder().id(1L).name("category").slug("category").level(1).build());
    User user = User.of("user", "user", "image.png");
    user.setId(1L);
    gifticon.setSeller(user);
    Brand brand = Brand.builder().name("brand").slug("brand").build();
    brand.setId(1L);
    gifticon.setBrand(brand);
    gifticon.setTags(List.of(Tag.builder().name("tag").slug("tag").build()));
    gifticon.setSlug("test");

    // createdAt 설정
    Field createdAtField = Gifticon.class.getSuperclass().getSuperclass().getDeclaredField("createdAt");
    createdAtField.setAccessible(true);
    createdAtField.set(gifticon, LocalDateTime.of(2025, 1, 1, 12, 0));

    // updatedAt 설정
    Field updatedAtField = Gifticon.class.getSuperclass().getDeclaredField("updatedAt");
    updatedAtField.setAccessible(true);
    updatedAtField.set(gifticon, LocalDateTime.of(2025, 1, 2, 15, 0));
    ReviewDto.ReviewSummary reviewSummary = ReviewDto.ReviewSummary.builder()
            .averageRating(0D)
            .totalCount(0)
            .distribution(Map.of(1, 0, 2, 0, 3, 0, 4, 0, 5, 0))
            .build();

    // when
    GifticonQueryDto.Gifticon gifticonDto = mapper.toGifticon(gifticon, reviewSummary);

    // then
    assertEquals(1L, gifticonDto.getId());
    assertEquals("test", gifticonDto.getName());
    assertEquals("description", gifticonDto.getDescription());
    assertEquals(GifticonStatus.ON_SALE.toString(), gifticonDto.getStatus());
    assertEquals(LocalDateTime.of(2025, 1, 1, 12, 0), gifticonDto.getCreatedAt());
    assertEquals(LocalDateTime.of(2025, 1, 2, 15, 0), gifticonDto.getUpdatedAt());
  }

  @DisplayName("GifticonQuery response 변환 테스트 2")
  @Test
  void testToGifticon() {
    // given
    GifticonDocument gifticonDocument = GifticonDocument.builder()
            .id(1L)
            .name("name")
            .description("description")
            .status(GifticonStatus.ON_SALE.toString())
            .createdAt(LocalDateTime.of(2025, 1, 1, 12, 0))
            .updatedAt(LocalDateTime.of(2025, 1, 2, 15, 0))
            .seller(Map.of("id", 1L, "nickname", "nick"))
            .brand(Map.of("id", 1L, "name", "brand"))
            .price(Map.of("basePrice", BigDecimal.ZERO, "salePrice", BigDecimal.ZERO, "currency", "KRW", "discountPercentage", 0))
            .category(Map.of("id", 1L, "name", "category", "slug", "category", "parent", Map.of()))
            .build();
    UserRatingSummary userRatingSummary = UserRatingSummary.builder()
            .id(1L)
            .averageRating(0D)
            .totalCount(0)
            .distribution(Map.of(1, 0, 2, 0, 3, 0, 4, 0, 5, 0))
            .build();

    // when
    GifticonQueryDto.Gifticon gifticon = mapper.toGifticon(gifticonDocument, userRatingSummary);

    // then
    assertEquals(1L, gifticon.getId());
    assertEquals("name", gifticon.getName());
    assertEquals("description", gifticon.getDescription());
    assertEquals(GifticonStatus.ON_SALE.toString(), gifticon.getStatus());
    assertEquals(LocalDateTime.of(2025, 1, 1, 12, 0), gifticon.getCreatedAt());
    assertEquals(LocalDateTime.of(2025, 1, 2, 15, 0), gifticon.getUpdatedAt());
    assertEquals(userRatingSummary.getAverageRating(), gifticon.getSellerSummary().getAverage());
    assertEquals(userRatingSummary.getTotalCount(), gifticon.getSellerSummary().getReviewCount());
    assertEquals(userRatingSummary.getDistribution(), gifticon.getSellerSummary().getDistribution());

  }
}