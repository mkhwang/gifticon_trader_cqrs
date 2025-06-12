package com.mkhwang.trader.sync.application.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkhwang.trader.sync.application.GifticonDocumentModelSyncer;
import com.mkhwang.trader.sync.application.handler.document.GifticonDocumentModelEventHandler;
import com.mkhwang.trader.sync.application.handler.dto.CdcEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GifticonDocumentModelSyncerTest {

  @Mock
  ObjectMapper objectMapper;

  @Mock
  GifticonDocumentModelEventHandler handler1;

  @Mock
  GifticonDocumentModelEventHandler handler2;

  GifticonDocumentModelSyncer syncer;

  @BeforeEach
  void setUp() {
    syncer = new GifticonDocumentModelSyncer(objectMapper, List.of(handler1, handler2));
  }

  @DisplayName("생성 이벤트 성공 테스트")
  @Test
  void shouldHandleCreateEventSuccessfully() throws Exception {
    // given
    String messageValue = "{\"op\":\"c\",\"source\":{\"table\":\"gifticon\"}}";
    String messageKey = "{\"id\":100}";

    CdcEvent event = new CdcEvent();
    event.setOp("c");
    CdcEvent.Source source = new CdcEvent.Source();
    source.setTable("gifticon");
    event.setSource(source);

    Map<String, Object> keyMap = Map.of("id", 100);
    event.setKey(keyMap);

    given(objectMapper.readValue(messageValue, CdcEvent.class)).willReturn(event);
    given(handler1.canHandle(event)).willReturn(false);
    given(handler2.canHandle(event)).willReturn(true);

    // when
    syncer.consumeDocumentEvents(messageValue, messageKey, "gifticon-events");

    // then
    verify(handler2).handle(event);
    verify(handler1, never()).handle(any());
  }

  @DisplayName("삭제 이벤트 테스트")
  @Test
  void testConsume_deleteEvent_handled() throws Exception {
    // given
    String messageValue = "{\"op\":\"d\",\"source\":{\"table\":\"gifticon\"}}";
    CdcEvent event = new CdcEvent();
    event.setOp("d");
    event.setSource(new CdcEvent.Source());
    event.getSource().setTable("gifticon");

    given(objectMapper.readValue(messageValue, CdcEvent.class)).willReturn(event);
    given(handler1.canHandle(event)).willReturn(true);

    // when
    syncer.consumeDocumentEvents(messageValue, null, "gifticon-events");

    // then
    verify(handler1).handle(event);
    assertTrue(event.isDelete());
  }

  @DisplayName("이벤트 json 파싱 실패 테스트")
  @Test
  void testConsume_keyParsingFails_gracefully() throws Exception {
    // given
    String messageValue = "{\"op\":\"u\",\"source\":{\"table\":\"gifticon\"}}";
    String messageKey = "INVALID_JSON";

    CdcEvent event = new CdcEvent();
    event.setOp("u");
    event.setSource(new CdcEvent.Source());
    event.getSource().setTable("gifticon");

    given(objectMapper.readValue(messageValue, CdcEvent.class)).willThrow(new JsonProcessingException("bad key") {
    });

    // when
    syncer.consumeDocumentEvents(messageValue, messageKey, "gifticon-events");

    // then
    verify(handler1, atLeast(0)).canHandle(event);
  }
}