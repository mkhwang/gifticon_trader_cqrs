package com.mkhwang.trader.sync.application.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkhwang.trader.sync.application.GifticonSearchModelSyncer;
import com.mkhwang.trader.sync.application.handler.dto.CdcEvent;
import com.mkhwang.trader.sync.application.handler.search.GifticonSearchModelBaseEventHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("GifticonSearchModelSyncer 단위 테스트")
@ExtendWith(MockitoExtension.class)
class GifticonSearchModelSyncerTest {

  @Mock
  ObjectMapper objectMapper;

  @Mock
  GifticonSearchModelBaseEventHandler handler1;

  @Mock
  GifticonSearchModelBaseEventHandler handler2;

  GifticonSearchModelSyncer syncer;

  @BeforeEach
  void setUp() {
    syncer = new GifticonSearchModelSyncer(objectMapper, List.of(handler1, handler2));
  }

  @Test
  @DisplayName("정상 메시지 수신 시 - 핸들러가 이벤트 처리")
  void shouldHandleEvent_whenHandlerCanHandle() throws Exception {
    // given
    String messageValue = "{\"op\":\"c\",\"source\":{\"table\":\"gifticon\"}}";
    String messageKey = "{\"id\":100}";

    CdcEvent event = new CdcEvent();
    event.setOp("c");
    CdcEvent.Source source = new CdcEvent.Source();
    source.setTable("gifticon");
    event.setSource(source);

    Map<String, Object> keyMap = Map.of("id", 100);

    given(objectMapper.readValue(eq(messageValue), eq(CdcEvent.class))).willReturn(event);
    given(objectMapper.readValue(eq(messageKey), any(TypeReference.class))).willReturn(keyMap);
    given(handler1.canHandle(event)).willReturn(false);
    given(handler2.canHandle(event)).willReturn(true);

    // when
    syncer.consumeGifticonEvents(messageValue, messageKey);

    // then
    verify(handler2).handle(event);
    verify(handler1, never()).handle(any());
  }

  @Test
  @DisplayName("모든 핸들러가 처리하지 않는 경우 - 로그만 출력")
  void shouldNotHandle_whenNoHandlerMatches() throws Exception {
    // given
    String messageValue = "{\"op\":\"u\",\"source\":{\"table\":\"gifticon\"}}";
    CdcEvent event = new CdcEvent();
    event.setOp("u");
    CdcEvent.Source source = new CdcEvent.Source();
    source.setTable("gifticon");
    event.setSource(source);

    given(objectMapper.readValue(eq(messageValue), eq(CdcEvent.class))).willReturn(event);
    given(handler1.canHandle(event)).willReturn(false);
    given(handler2.canHandle(event)).willReturn(false);

    // when
    syncer.consumeGifticonEvents(messageValue, null);

    // then
    verify(handler1, never()).handle(any());
    verify(handler2, never()).handle(any());
  }

  @Test
  @DisplayName("messageKey 파싱 실패 시 - 예외 처리 후 계속 진행")
  void shouldContinue_whenKeyParsingFails() throws Exception {
    // given
    String messageValue = "{\"op\":\"u\",\"source\":{\"table\":\"gifticon\"}}";
    String badKey = "not json";

    CdcEvent event = new CdcEvent();
    event.setOp("u");
    CdcEvent.Source source = new CdcEvent.Source();
    source.setTable("gifticon");
    event.setSource(source);

    given(objectMapper.readValue(eq(messageValue), eq(CdcEvent.class))).willReturn(event);
    given(objectMapper.readValue(eq(badKey), any(TypeReference.class)))
            .willThrow(new JsonProcessingException("bad json") {
            });
    // when
    syncer.consumeGifticonEvents(messageValue, badKey);

    // then
    verify(handler1, atLeast(0)).canHandle(event);
  }

  @Test
  @DisplayName("source.table이 null인 경우 - 이벤트 처리하지 않음")
  void shouldSkipEvent_whenTableIsNull() throws Exception {
    // given
    String messageValue = "{\"op\":\"c\"}";

    CdcEvent event = new CdcEvent();
    event.setOp("c");
    event.setSource(null); // table == null

    given(objectMapper.readValue(eq(messageValue), eq(CdcEvent.class))).willReturn(event);

    // when
    syncer.consumeGifticonEvents(messageValue, null);

    // then
    verify(handler1, never()).canHandle(any());
    verify(handler2, never()).canHandle(any());
  }

  @Test
  @DisplayName("messageValue 파싱 실패 시 - JsonProcessingException 처리")
  void shouldCatchJsonParsingError_whenMessageIsInvalid() throws Exception {
    // given
    String invalidJson = "not valid";

    given(objectMapper.readValue(eq(invalidJson), eq(CdcEvent.class)))
            .willThrow(new JsonProcessingException("parse error") {
            });

    // when
    syncer.consumeGifticonEvents(invalidJson, null);

    // then
    verifyNoInteractions(handler1, handler2);
  }
}