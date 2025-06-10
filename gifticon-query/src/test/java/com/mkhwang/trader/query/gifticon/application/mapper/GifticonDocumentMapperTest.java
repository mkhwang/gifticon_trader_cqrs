package com.mkhwang.trader.query.gifticon.application.mapper;

import com.mkhwang.trader.common.brand.domain.Brand;
import com.mkhwang.trader.common.category.domain.Category;
import com.mkhwang.trader.common.gifticon.domain.Gifticon;
import com.mkhwang.trader.common.gifticon.domain.GifticonPrice;
import com.mkhwang.trader.common.gifticon.domain.GifticonStatus;
import com.mkhwang.trader.common.tag.domain.Tag;
import com.mkhwang.trader.common.user.domain.User;
import com.mkhwang.trader.query.gifticon.application.dto.GifticonQueryDto;
import com.mkhwang.trader.query.gifticon.domain.GifticonDocument;
import com.mkhwang.trader.query.gifticon.domain.GifticonSearchDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class GifticonDocumentMapperTest {
  private Gifticon gifticon;

  private final GifticonDocumentMapper gifticonDocumentMapper = new GifticonDocumentMapper();


  @BeforeEach
  public void setUp() throws NoSuchFieldException, IllegalAccessException {
    gifticon = new Gifticon();
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
  }

  @DisplayName("Gifticon Entity 를 SearchDocument로 변환하는 테스트")
  @Test
  void toSearchDocument() {
    // given

    // when
    GifticonSearchDocument searchDocument = gifticonDocumentMapper.toSearchDocument(this.gifticon);


    // then
    assertNotNull(searchDocument);
    assertEquals(1L, searchDocument.getId());
    assertEquals("test", searchDocument.getName());
    assertEquals("description", searchDocument.getDescription());
    assertEquals(GifticonStatus.ON_SALE.toString(), searchDocument.getStatus());
    assertEquals(BigDecimal.ZERO, searchDocument.getBasePrice());
    assertEquals(BigDecimal.ZERO, searchDocument.getSalePrice());
    assertEquals("user", searchDocument.getSeller());
    assertEquals("brand", searchDocument.getBrand());
    assertEquals(List.of("tag"), searchDocument.getTags());
    assertEquals("test", searchDocument.getSlug());
  }

  @DisplayName("Gifticon Entity 를 Document로 변환하는 테스트")
  @Test
  void toDocument() {
    // given

    // when
    GifticonDocument document = gifticonDocumentMapper.toDocument(this.gifticon);

    // then
    assertNotNull(document);
    assertEquals(1L, document.getId());
    assertEquals("test", document.getName());
    assertEquals("description", document.getDescription());
    assertEquals(GifticonStatus.ON_SALE.toString(), document.getStatus());
    assertEquals(Map.of("basePrice", BigDecimal.ZERO, "salePrice", BigDecimal.ZERO, "currency", "KRW", "discountPercentage", 0), document.getPrice());
    assertEquals("test", document.getSlug());
  }

  @DisplayName("GifticonDocument를 summary로 변환하는 테스트")
  @Test
  void toSummary() {
    // given
    GifticonDocument gifticonDocument = gifticonDocumentMapper.toDocument(this.gifticon);

    // when
    GifticonQueryDto.GifticonSummary summary = gifticonDocumentMapper.toSummary(gifticonDocument);

    // then
    assertNotNull(summary);
    assertEquals(1L, summary.getId());
    assertEquals("test", summary.getName());
    assertEquals("description", summary.getDescription());
    assertEquals(GifticonStatus.ON_SALE.toString(), summary.getStatus());
    assertEquals(BigDecimal.ZERO, summary.getBasePrice());
    assertEquals(BigDecimal.ZERO, summary.getSalePrice());
  }
}