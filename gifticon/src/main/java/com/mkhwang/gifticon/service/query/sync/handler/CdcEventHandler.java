package com.mkhwang.gifticon.service.query.sync.handler;


import com.mkhwang.gifticon.service.query.sync.CdcEvent;

public interface CdcEventHandler {

    boolean canHandle(CdcEvent event);

    void handle(CdcEvent event);
}