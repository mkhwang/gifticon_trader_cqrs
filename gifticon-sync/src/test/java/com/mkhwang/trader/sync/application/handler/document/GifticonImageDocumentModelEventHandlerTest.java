package com.mkhwang.trader.sync.application.handler.document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkhwang.trader.query.gifticon.domain.GifticonDocument;
import com.mkhwang.trader.query.gifticon.infra.GifticonDocumentRepository;
import com.mkhwang.trader.sync.application.handler.dto.CdcEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("GifticonImageDocumentModelEventHandler 단위 테스트")
@ExtendWith(MockitoExtension.class)
class GifticonImageDocumentModelEventHandlerTest {

  @Mock
  GifticonDocumentRepository gifticonDocumentRepository;

  GifticonImageDocumentModelEventHandler handler;

  @BeforeEach
  void setUp() {
    handler = new GifticonImageDocumentModelEventHandler(new ObjectMapper(), gifticonDocumentRepository);
  }

  @Test
  @DisplayName("새 이미지 추가 시 - document.images에 추가되고 저장됨")
  void shouldAddNewImageToDocument() {
    // given
    Long gifticonId = 1L;
    Long imageId = 100L;

    Map<String, Object> after = Map.of(
            "id", imageId,
            "gifticon_id", gifticonId,
            "url", "http://img.png",
            "alt_text", "설명",
            "is_primary", true,
            "display_order", 1
    );

    CdcEvent event = new CdcEvent();
    event.setOp("c");
    event.setAfter(after);

    GifticonDocument doc = GifticonDocument.builder().id(gifticonId).images(new ArrayList<>()).build();

    given(gifticonDocumentRepository.findById(gifticonId)).willReturn(Optional.of(doc));

    // when
    handler.handle(event);

    // then
    assertEquals(1, doc.getImages().size());
    assertEquals(imageId, doc.getImages().get(0).get("id"));
    verify(gifticonDocumentRepository).save(doc);
  }

  @Test
  @DisplayName("기존 이미지가 있으면 업데이트되고 저장됨")
  void shouldUpdateExistingImageInDocument() {
    // given
    Long gifticonId = 1L;
    Long imageId = 100L;

    Map<String, Object> after = Map.of(
            "id", imageId,
            "gifticon_id", gifticonId,
            "url", "http://updated.png",
            "alt_text", "업데이트 설명",
            "is_primary", false,
            "display_order", 2
    );

    CdcEvent event = new CdcEvent();
    event.setOp("u");
    event.setAfter(after);

    Map<String, Object> existing = new HashMap<>();
    existing.put("id", imageId);
    existing.put("url", "http://old.png");

    GifticonDocument doc = GifticonDocument.builder()
            .id(gifticonId)
            .images(new ArrayList<>(List.of(existing)))
            .build();

    given(gifticonDocumentRepository.findById(gifticonId)).willReturn(Optional.of(doc));

    // when
    handler.handle(event);

    // then
    assertEquals(1, doc.getImages().size());
    assertEquals("http://updated.png", doc.getImages().get(0).get("url"));
    assertEquals("업데이트 설명", doc.getImages().get(0).get("altText"));
    verify(gifticonDocumentRepository).save(doc);
  }

  @Test
  @DisplayName("삭제 이벤트 처리 시 - 해당 이미지 제거 및 저장")
  void shouldRemoveImageOnDeleteEvent() {
    // given
    Long gifticonId = 1L;
    Long imageId = 100L;

    Map<String, Object> before = Map.of(
            "id", imageId,
            "gifticon_id", gifticonId
    );

    CdcEvent event = new CdcEvent();
    event.setOp("d");
    event.setBefore(before);

    Map<String, Object> existing = new HashMap<>();
    existing.put("id", imageId);

    GifticonDocument doc = GifticonDocument.builder()
            .id(gifticonId)
            .images(new ArrayList<>(List.of(existing)))
            .build();

    given(gifticonDocumentRepository.findById(gifticonId)).willReturn(Optional.of(doc));

    // when
    handler.handle(event);

    // then
    assertTrue(doc.getImages().isEmpty());
    verify(gifticonDocumentRepository).save(doc);
  }

  @Test
  @DisplayName("gifticon_id가 없으면 무시")
  void shouldSkipWhenGifticonIdMissing() {
    // given
    Map<String, Object> after = Map.of("id", 100L);
    CdcEvent event = new CdcEvent();
    event.setOp("c");
    event.setAfter(after);

    // when
    handler.handle(event);

    // then
    verifyNoInteractions(gifticonDocumentRepository);
  }

  @Test
  @DisplayName("gifticon 문서가 없으면 무시")
  void shouldSkipWhenGifticonDocumentNotFound() {
    // given
    Map<String, Object> after = Map.of(
            "id", 100L,
            "gifticon_id", 999L
    );
    CdcEvent event = new CdcEvent();
    event.setOp("u");
    event.setAfter(after);

    given(gifticonDocumentRepository.findById(999L)).willReturn(Optional.empty());

    // when
    handler.handle(event);

    // then
    verify(gifticonDocumentRepository, never()).save(any());
  }
}