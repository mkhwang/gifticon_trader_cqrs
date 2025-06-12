package com.mkhwang.trader.sync.application.handler.search;

import com.mkhwang.trader.query.gifticon.domain.GifticonSearchDocument;
import com.mkhwang.trader.query.gifticon.infra.GifticonSearchRepository;
import com.mkhwang.trader.sync.application.handler.dto.CdcEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GifticonTagSearchModelEventHandlerTest {

  @Mock
  private GifticonSearchRepository gifticonSearchRepository;

  @Mock
  private ElasticsearchOperations elasticsearchOperations;

  @InjectMocks
  private GifticonTagSearchModelEventHandler handler;

  @DisplayName("태그 삭제 이벤트 발생 시 태그가 제거되고 문서가 업데이트된다")
  @Test
  void shouldUpdateTagsOnDeleteEvent() {
    Map<String, Object> beforeData = Map.of(
            "gifticon_id", 1L,
            "tag_id", 10L,
            "name", "여름"
    );
    CdcEvent event = mock(CdcEvent.class);
    given(event.isDelete()).willReturn(true);
    given(event.getBeforeData()).willReturn(beforeData);

    GifticonSearchDocument document = new GifticonSearchDocument();
    document.setId(1L);
    document.setTags(new ArrayList<>(List.of("여름", "겨울")));

    given(gifticonSearchRepository.findById(1L)).willReturn(Optional.of(document));

    handler.handle(event);

    verify(elasticsearchOperations).update(any(UpdateQuery.class), eq(IndexCoordinates.of("gifticons")));
  }

  @DisplayName("문서가 존재하지 않는 경우 아무것도 하지 않음")
  @Test
  void shouldDoNothingIfDocumentNotFound() {
    Map<String, Object> beforeData = Map.of(
            "gifticon_id", 1L,
            "tag_id", 10L,
            "name", "한정판"
    );
    CdcEvent event = mock(CdcEvent.class);
    given(event.isDelete()).willReturn(true);
    given(event.getBeforeData()).willReturn(beforeData);
    given(gifticonSearchRepository.findById(1L)).willReturn(Optional.empty());

    handler.handle(event);

    verify(elasticsearchOperations, never()).update(any(), any());
  }

  @DisplayName("태그가 없어서 업데이트가 일어나지 않음")
  @Test
  void shouldNotUpdateIfTagDoesNotExist() {
    Map<String, Object> beforeData = Map.of(
            "gifticon_id", 2L,
            "tag_id", 20L,
            "name", "없는태그"
    );
    CdcEvent event = mock(CdcEvent.class);
    given(event.isDelete()).willReturn(true);
    given(event.getBeforeData()).willReturn(beforeData);

    GifticonSearchDocument document = new GifticonSearchDocument();
    document.setId(2L);
    document.setTags(new ArrayList<>(List.of("봄", "겨울")));

    given(gifticonSearchRepository.findById(2L)).willReturn(Optional.of(document));

    handler.handle(event);

    verify(elasticsearchOperations, never()).update(any(), any());
  }

  @DisplayName("입력 값이 null 또는 필수 필드가 빠진 경우 무시")
  @Test
  void shouldIgnoreIfRequiredFieldsMissing() {
    // case 1: null data
    CdcEvent event1 = mock(CdcEvent.class);
    given(event1.isDelete()).willReturn(true);
    given(event1.getBeforeData()).willReturn(null);
    handler.handle(event1);

    // case 2: missing gifticon_id
    CdcEvent event2 = mock(CdcEvent.class);
    given(event2.isDelete()).willReturn(true);
    given(event2.getBeforeData()).willReturn(Map.of("tag_id", 10L));
    handler.handle(event2);

    // case 3: missing tag_id
    CdcEvent event3 = mock(CdcEvent.class);
    given(event3.isDelete()).willReturn(true);
    given(event3.getBeforeData()).willReturn(Map.of("gifticon_id", 1L));
    handler.handle(event3);

    verifyNoInteractions(elasticsearchOperations);
  }

  @DisplayName("기존 태그 목록이 null인 경우 graceful 처리")
  @Test
  void shouldHandleNullTagListGracefully() {
    Map<String, Object> beforeData = Map.of(
            "gifticon_id", 1L,
            "tag_id", 10L,
            "name", "여름"
    );
    CdcEvent event = mock(CdcEvent.class);
    given(event.isDelete()).willReturn(true);
    given(event.getBeforeData()).willReturn(beforeData);

    GifticonSearchDocument document = new GifticonSearchDocument();
    document.setId(1L);
    document.setTags(null); // 중요 포인트

    given(gifticonSearchRepository.findById(1L)).willReturn(Optional.of(document));

    handler.handle(event);

    verify(elasticsearchOperations, never()).update(any(), any());
  }
}