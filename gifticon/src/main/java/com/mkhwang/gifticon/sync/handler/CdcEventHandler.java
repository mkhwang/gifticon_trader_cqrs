package com.mkhwang.gifticon.sync.handler;


import com.mkhwang.gifticon.sync.handler.dto.CdcEvent;

public interface CdcEventHandler {

    boolean canHandle(CdcEvent event);

    void handle(CdcEvent event);
}