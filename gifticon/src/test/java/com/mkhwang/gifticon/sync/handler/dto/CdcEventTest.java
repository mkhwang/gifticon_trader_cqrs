package com.mkhwang.gifticon.sync.handler.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CdcEventTest {

  @Test
  void isDelete() {
    // given
    CdcEvent event = new CdcEvent();
    event.setOp("d");

    // when & then
    assertTrue(event.isDelete());
    event.setOp("c");
    assertFalse(event.isDelete());
  }

  @Test
  void isCreate() {
    // given
    CdcEvent event = new CdcEvent();
    event.setOp("c");

    // when & then
    assertTrue(event.isCreate());
    event.setOp("d");
    assertFalse(event.isCreate());
  }

  @Test
  void isUpdate() {
    // given
    CdcEvent event = new CdcEvent();
    event.setOp("u");

    // when & then
    assertTrue(event.isUpdate());
    event.setOp("d");
    assertFalse(event.isUpdate());
  }

  @Test
  void isRead() {
    // given
    CdcEvent event = new CdcEvent();
    event.setOp("r");

    // when & then
    assertTrue(event.isRead());
    event.setOp("d");
    assertFalse(event.isRead());
  }
}