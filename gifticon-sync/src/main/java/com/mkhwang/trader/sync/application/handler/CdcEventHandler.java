package com.mkhwang.trader.sync.application.handler;


import com.mkhwang.trader.sync.application.handler.dto.CdcEvent;

public interface CdcEventHandler {

  boolean canHandle(CdcEvent event);

  void handle(CdcEvent event);
}