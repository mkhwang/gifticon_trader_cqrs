package com.mkhwang.trader.sync.application.handler.document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkhwang.trader.common.brand.domain.Brand;
import com.mkhwang.trader.common.brand.infra.BrandRepository;
import com.mkhwang.trader.common.user.domain.User;
import com.mkhwang.trader.common.user.infra.UserRepository;
import com.mkhwang.trader.query.gifticon.domain.GifticonDocument;
import com.mkhwang.trader.query.gifticon.infra.GifticonDocumentRepository;
import com.mkhwang.trader.sync.application.handler.dto.CdcEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@DisplayName("GifticonDocumentModelEventHandler 단위 테스트")
@ExtendWith(MockitoExtension.class)
class GifticonDocumentModelEventHandlerTest {

  @Mock
  ObjectMapper objectMapper;
  @Mock
  GifticonDocumentRepository gifticonDocumentRepository;
  @Mock
  BrandRepository brandRepository;
  @Mock
  UserRepository userRepository;

  GifticonDocumentModelEventHandler handler;

  @BeforeEach
  void setUp() {
    handler = new GifticonDocumentModelEventHandler(
            objectMapper,
            gifticonDocumentRepository,
            brandRepository,
            userRepository
    );
  }

  @Test
  @DisplayName("삭제 이벤트 처리 시 - deleteById 호출")
  void shouldDeleteDocumentOnDeleteEvent() {
    // given
    Map<String, Object> before = Map.of("id", 123L);
    CdcEvent event = new CdcEvent();
    event.setOp("d");
    event.setBefore(before);

    // when
    handler.handle(event);

    // then
    verify(gifticonDocumentRepository).deleteById(123L);
  }

  @Test
  @DisplayName("afterData가 존재하고, 문서가 없으면 새로 생성 후 저장")
  void shouldCreateNewDocumentWhenNotFound() {
    // given
    Long id = 1L;
    Map<String, Object> after = new HashMap<>();
    after.put("id", id);
    after.put("name", "gifticon");
    after.put("slug", "gifticon-slug");
    after.put("status", "active");

    CdcEvent event = new CdcEvent();
    event.setOp("c");
    event.setAfter(after);

    given(gifticonDocumentRepository.findById(id)).willReturn(Optional.empty());

    // when
    handler.handle(event);

    // then
    ArgumentCaptor<GifticonDocument> captor = ArgumentCaptor.forClass(GifticonDocument.class);
    verify(gifticonDocumentRepository).save(captor.capture());

    GifticonDocument saved = captor.getValue();
    assertEquals(id, saved.getId());
    assertEquals("gifticon", saved.getName());
    assertEquals("gifticon-slug", saved.getSlug());
    assertEquals("active", saved.getStatus());
  }

  @Test
  @DisplayName("seller_id 존재 시 userRepository에서 조회 후 seller 정보 설정")
  void shouldSetSellerFromUserRepository() {
    // given
    Long sellerId = 99L;
    Map<String, Object> after = new HashMap<>();
    after.put("id", 1L);
    after.put("seller_id", sellerId);

    User user = User.of("username", "nick", "image.com");
    user.setId(sellerId);

    CdcEvent event = new CdcEvent();
    event.setOp("u");
    event.setAfter(after);

    GifticonDocument doc = GifticonDocument.builder().id(1L).build();

    given(gifticonDocumentRepository.findById(1L)).willReturn(Optional.of(doc));
    given(userRepository.findById(sellerId)).willReturn(Optional.of(user));

    // when
    handler.handle(event);

    // then
    verify(userRepository).findById(sellerId);
    verify(gifticonDocumentRepository).save(any());
    assertEquals(Map.of("id", 99L, "nickName", "nick"), doc.getSeller());
  }

  @Test
  @DisplayName("brand_id 존재 시 brandRepository에서 조회 후 brand 정보 설정")
  void shouldSetBrandFromBrandRepository() {
    // given
    Long brandId = 7L;
    Map<String, Object> after = new HashMap<>();
    after.put("id", 1L);
    after.put("brand_id", brandId);

    Brand brand = new Brand();
    brand.setId(brandId);
    brand.setName("브랜드");

    CdcEvent event = new CdcEvent();
    event.setOp("u");
    event.setAfter(after);

    GifticonDocument doc = GifticonDocument.builder().id(1L).build();

    given(gifticonDocumentRepository.findById(1L)).willReturn(Optional.of(doc));
    given(brandRepository.findById(brandId)).willReturn(Optional.of(brand));

    // when
    handler.handle(event);

    // then
    verify(brandRepository).findById(brandId);
    verify(gifticonDocumentRepository).save(any());
    assertEquals(Map.of("id", brandId, "name", "브랜드"), doc.getBrand());
  }

  @Test
  @DisplayName("필수 필드(id)가 없는 경우 - 저장/삭제 안 함")
  void shouldNotSaveWhenNoIdProvided() {
    // given
    Map<String, Object> data = new HashMap<>();
    data.put("name", "no-id");

    CdcEvent event = new CdcEvent();
    event.setOp("c");
    event.setAfter(data);

    // when
    handler.handle(event);

    // then
    verify(gifticonDocumentRepository, never()).save(any());
    verify(gifticonDocumentRepository, never()).deleteById(any());
  }
}