package com.mkhwang.trader.sync.application.handler.document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkhwang.trader.common.tag.domain.Tag;
import com.mkhwang.trader.common.tag.infra.TagRepository;
import com.mkhwang.trader.query.gifticon.domain.GifticonDocument;
import com.mkhwang.trader.query.gifticon.infra.GifticonDocumentRepository;
import com.mkhwang.trader.sync.application.handler.dto.CdcEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("GifticonTagDocumentModelEventHandler 단위 테스트")
@ExtendWith(MockitoExtension.class)
class GifticonTagDocumentModelEventHandlerTest {

  @Mock
  GifticonDocumentRepository gifticonDocumentRepository;
  @Mock
  TagRepository tagRepository;

  GifticonTagDocumentModelEventHandler handler;

  @BeforeEach
  void setUp() {
    handler = new GifticonTagDocumentModelEventHandler(new ObjectMapper(), gifticonDocumentRepository, tagRepository);
  }

  @Test
  @DisplayName("삭제 이벤트 시 - 해당 tag 제거 및 저장")
  void shouldRemoveTagOnDeleteEvent() {
    Long gifticonId = 1L, tagId = 99L;

    Map<String, Object> before = Map.of("gifticon_id", gifticonId, "tag_id", tagId);
    CdcEvent event = new CdcEvent();
    event.setOp("d");
    event.setBefore(before);

    Map<String, Object> tag = Map.of("id", tagId, "name", "할인");
    GifticonDocument doc = GifticonDocument.builder()
            .id(gifticonId)
            .tags(new ArrayList<>(List.of(tag)))
            .build();

    given(gifticonDocumentRepository.findById(gifticonId)).willReturn(Optional.of(doc));

    // when
    handler.handle(event);

    // then
    assertTrue(doc.getTags().isEmpty());
    verify(gifticonDocumentRepository).save(doc);
  }

  @Test
  @DisplayName("태그가 존재하지 않을 때 추가 및 저장")
  void shouldAddTagWhenNotExists() {
    // given
    Long gifticonId = 1L, tagId = 99L;

    Map<String, Object> after = Map.of("gifticon_id", gifticonId, "tag_id", tagId);
    CdcEvent event = new CdcEvent();
    event.setOp("c");
    event.setAfter(after);

    GifticonDocument doc = GifticonDocument.builder()
            .id(gifticonId)
            .tags(new ArrayList<>())
            .build();

    Tag tag = new Tag();
    tag.setId(tagId);
    tag.setName("신제품");
    tag.setSlug("new");

    given(gifticonDocumentRepository.findById(gifticonId)).willReturn(Optional.of(doc));
    given(tagRepository.findById(tagId)).willReturn(Optional.of(tag));

    // when
    handler.handle(event);

    // then
    assertEquals(1, doc.getTags().size());
    assertEquals(tagId, doc.getTags().get(0).get("id"));
    verify(gifticonDocumentRepository).save(doc);
  }

  @Test
  @DisplayName("이미 존재하는 태그면 추가하지 않음")
  void shouldNotAddDuplicateTag() {
    // given
    Long gifticonId = 1L, tagId = 77L;

    Map<String, Object> after = Map.of("gifticon_id", gifticonId, "tag_id", tagId);
    CdcEvent event = new CdcEvent();
    event.setOp("u");
    event.setAfter(after);

    Map<String, Object> existingTag = Map.of("id", tagId, "name", "중복", "slug", "dup");
    GifticonDocument doc = GifticonDocument.builder()
            .id(gifticonId)
            .tags(new ArrayList<>(List.of(existingTag)))
            .build();

    given(gifticonDocumentRepository.findById(gifticonId)).willReturn(Optional.of(doc));

    // when
    handler.handle(event);

    // then
    verify(gifticonDocumentRepository, never()).save(any());
  }

  @Test
  @DisplayName("gifticon_id나 tag_id 없으면 무시")
  void shouldSkipIfGifticonIdOrTagIdMissing() {
    // given
    CdcEvent event = new CdcEvent();
    event.setOp("c");
    event.setAfter(Map.of("gifticon_id", 1L)); // tag_id 없음

    // when & then
    handler.handle(event);
    verifyNoInteractions(gifticonDocumentRepository);

    event.setAfter(Map.of("tag_id", 1L)); // gifticon_id 없음
    handler.handle(event);
    verifyNoInteractions(gifticonDocumentRepository);
  }

  @Test
  @DisplayName("gifticon 문서 없으면 무시")
  void shouldSkipIfGifticonDocumentNotFound() {
    // given
    CdcEvent event = new CdcEvent();
    event.setOp("c");
    event.setAfter(Map.of("gifticon_id", 1L, "tag_id", 2L));

    given(gifticonDocumentRepository.findById(1L)).willReturn(Optional.empty());

    // when
    handler.handle(event);

    // then
    verify(tagRepository, never()).findById(any());
    verify(gifticonDocumentRepository, never()).save(any());
  }

  @Test
  @DisplayName("tagRepository 결과 없으면 추가하지 않음")
  void shouldNotAddIfTagNotFound() {
    // given
    Long gifticonId = 1L, tagId = 2L;

    CdcEvent event = new CdcEvent();
    event.setOp("c");
    event.setAfter(Map.of("gifticon_id", gifticonId, "tag_id", tagId));

    GifticonDocument doc = GifticonDocument.builder()
            .id(gifticonId)
            .tags(new ArrayList<>())
            .build();

    given(gifticonDocumentRepository.findById(gifticonId)).willReturn(Optional.of(doc));
    given(tagRepository.findById(tagId)).willReturn(Optional.empty());

    // when
    handler.handle(event);

    // then
    assertTrue(doc.getTags().isEmpty());
    verify(gifticonDocumentRepository, never()).save(any());
  }
}