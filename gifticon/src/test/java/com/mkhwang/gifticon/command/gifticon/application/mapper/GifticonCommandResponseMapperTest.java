package com.mkhwang.gifticon.command.gifticon.application.mapper;

import com.mkhwang.gifticon.command.gifticon.presentation.dto.GifticonDto;
import com.mkhwang.gifticon.command.tag.domain.Tag;
import com.mkhwang.gifticon.query.brand.domain.Brand;
import com.mkhwang.gifticon.query.user.domain.User;
import com.mkhwang.gifticon.command.gifticon.domain.Gifticon;
import com.mkhwang.gifticon.command.gifticon.domain.GifticonPrice;
import com.mkhwang.gifticon.command.gifticon.domain.GifticonImage;
import com.mkhwang.gifticon.command.gifticon.domain.GifticonStatus;
import com.mkhwang.gifticon.query.category.domain.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GifticonCommandResponseMapperTest {

  private final GifticonCommandResponseMapper mapper = new GifticonCommandResponseMapper();

  @DisplayName("Gifticon Dto 변환 테스트")
  @Test
  void toDto() {
    // given
    Brand brand = Brand.builder().id(1L).name("스타벅스").build();
    User seller = User.of("name", "nickname", "profileImageUrl");
    seller.setId(2L); // Set ID for the seller

    Category category = new Category();
    category.setId(3L);
    category.setName("음료");
    category.setSlug("beverage");

    Gifticon gifticon = Gifticon.builder()
            .id(99L)
            .name("아메리카노")
            .slug("americano")
            .description("맛있는 커피")
            .category(category)
            .brand(brand)
            .seller(seller)
            .status(GifticonStatus.ON_SALE)
            .build();

    GifticonPrice price = GifticonPrice.builder()
            .basePrice(BigDecimal.valueOf(10000))
            .salePrice(BigDecimal.valueOf(8000))
            .currency("KRW")
            .build();

    List<GifticonImage> images = List.of(
            new GifticonImage(1L, gifticon, "url1", "alt1", true, 1),
            new GifticonImage(2L, gifticon, "url2", "alt2", false,2)
    );

    List<Tag> tags = List.of(
            new Tag(10L, "커피", "coffee"),
            new Tag(11L, "음료", "drink")
    );
    gifticon.setPrice(price);
    gifticon.setImages(images);
    gifticon.setTags(tags);

    // when
    GifticonDto.Gifticon dto = mapper.toDto(gifticon);

    // then
    assertEquals(99L, dto.getId());
    assertEquals("아메리카노", dto.getName());
    assertEquals("스타벅스", dto.getBrand().getName());
    assertEquals(BigDecimal.valueOf(10000), dto.getPrice().getBasePrice());
    assertEquals(2, dto.getImages().size());
    assertEquals(2, dto.getTags().size());
    assertEquals("커피", dto.getTags().get(0).getName());
    assertEquals("음료", dto.getCategory().getName());
  }
}