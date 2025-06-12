package com.mkhwang.trader.sync.application.handler.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkhwang.trader.common.brand.domain.Brand;
import com.mkhwang.trader.common.brand.infra.BrandRepository;
import com.mkhwang.trader.common.category.domain.Category;
import com.mkhwang.trader.common.category.infra.CategoryRepository;
import com.mkhwang.trader.common.user.domain.User;
import com.mkhwang.trader.common.user.infra.UserRepository;
import com.mkhwang.trader.query.gifticon.domain.GifticonSearchDocument;
import com.mkhwang.trader.query.gifticon.infra.GifticonSearchRepository;
import com.mkhwang.trader.sync.application.handler.dto.CdcEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;


@DisplayName("GifticonSearchModelEventandler 단위 테스트")
@ExtendWith(MockitoExtension.class)
class GifticonSearchModelEventandlerTest {

  @Mock
  GifticonSearchRepository gifticonSearchRepository;
  @Mock
  UserRepository userRepository;
  @Mock
  BrandRepository brandRepository;
  @Mock
  CategoryRepository categoryRepository;

  GifticonSearchModelEventandler handler;

  @BeforeEach
  void setUp() {
    handler = new GifticonSearchModelEventandler(
            new ObjectMapper(),
            gifticonSearchRepository,
            userRepository,
            brandRepository,
            categoryRepository
    );
  }

  @Test
  @DisplayName("삭제 이벤트 처리 시 deleteById 호출")
  void shouldDeleteDocumentOnDeleteEvent() {
    Long id = 1L;
    CdcEvent event = new CdcEvent();
    event.setOp("d");
    event.setBefore(Map.of("id", id));

    handler.handle(event);

    verify(gifticonSearchRepository).deleteById(id);
  }

  @Test
  @DisplayName("생성 이벤트 시 문서 생성 후 save 호출")
  void shouldCreateDocumentAndSave() {
    Long id = 1L;
    Long sellerId = 10L;
    Long brandId = 20L;
    Long categoryId = 30L;

    CdcEvent event = new CdcEvent();
    event.setOp("c");
    event.setAfter(Map.of(
            "id", id,
            "name", "테스트기프티콘",
            "slug", "slug",
            "description", "설명",
            "status", "active",
            "seller_id", sellerId,
            "brand_id", brandId,
            "category_id", categoryId,
            "created_at", "2025-06-12T12:00:00",
            "updated_at", "2025-06-12T13:00:00"
    ));

    User seller = User.of("username", "판매자", "image.profile.com");
    seller.setId(sellerId);

    Brand brand = new Brand();
    brand.setId(brandId);
    brand.setName("브랜드");

    Category category = new Category();
    category.setId(categoryId);
    category.setName("카테고리");

    given(userRepository.findById(sellerId)).willReturn(Optional.of(seller));
    given(brandRepository.findById(brandId)).willReturn(Optional.of(brand));
    given(categoryRepository.findById(categoryId)).willReturn(Optional.of(category));
    given(gifticonSearchRepository.findById(id)).willReturn(Optional.empty());

    handler.handle(event);

    ArgumentCaptor<GifticonSearchDocument> captor = ArgumentCaptor.forClass(GifticonSearchDocument.class);
    verify(gifticonSearchRepository).save(captor.capture());

    GifticonSearchDocument saved = captor.getValue();
    assertEquals("테스트기프티콘", saved.getName());
    assertEquals("판매자", saved.getSeller());
    assertEquals("브랜드", saved.getBrand());
    assertEquals("카테고리", saved.getCategory());
    assertNotNull(saved.getCreatedAt());
    assertNotNull(saved.getUpdatedAt());
  }

  @Test
  @DisplayName("기존 문서가 존재하면 필드 복원")
  void shouldRestoreExistingFields() {
    Long id = 1L;
    CdcEvent event = new CdcEvent();
    event.setOp("u");
    event.setAfter(Map.of("id", id));

    GifticonSearchDocument existing = new GifticonSearchDocument();
    existing.setBasePrice(new BigDecimal("10000"));
    existing.setSalePrice(new BigDecimal("8000"));
    existing.setTags(List.of("tag"));

    given(gifticonSearchRepository.findById(id)).willReturn(Optional.of(existing));

    handler.handle(event);

    ArgumentCaptor<GifticonSearchDocument> captor = ArgumentCaptor.forClass(GifticonSearchDocument.class);
    verify(gifticonSearchRepository).save(captor.capture());

    GifticonSearchDocument saved = captor.getValue();
    assertEquals(new BigDecimal("10000"), saved.getBasePrice());
    assertEquals(new BigDecimal("8000"), saved.getSalePrice());
    assertEquals(1, saved.getTags().size());
  }

  @Test
  @DisplayName("id가 없으면 아무 처리 안 함")
  void shouldSkipWhenIdMissing() {
    CdcEvent event = new CdcEvent();
    event.setOp("c");
    event.setAfter(Map.of("name", "기프티콘"));

    handler.handle(event);

    verifyNoInteractions(gifticonSearchRepository);
  }

  @Test
  @DisplayName("buyer_id 있을 경우 buyer 닉네임 설정")
  void shouldSetBuyerNicknameIfPresent() {
    Long id = 1L;
    Long buyerId = 55L;
    Long sellerId = 20L;

    User buyer = User.of("username", "구매자", "image.profile.com");
    buyer.setId(buyerId);

    User seller = User.of("username", "판매자", "image.profile.com");
    seller.setId(sellerId);

    CdcEvent event = new CdcEvent();
    event.setOp("u");
    event.setAfter(Map.of("id", id, "buyer_id", buyerId, "seller_id", sellerId));


    given(userRepository.findById(buyerId)).willReturn(Optional.of(buyer));
    given(userRepository.findById(sellerId)).willReturn(Optional.of(seller));
    given(gifticonSearchRepository.findById(id)).willReturn(Optional.empty());


    handler.handle(event);

    ArgumentCaptor<GifticonSearchDocument> captor = ArgumentCaptor.forClass(GifticonSearchDocument.class);
    verify(gifticonSearchRepository).save(captor.capture());

    GifticonSearchDocument saved = captor.getValue();
    assertEquals("구매자", saved.getBuyer());
  }
}