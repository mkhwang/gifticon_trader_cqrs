package com.mkhwang.gifticon.command.gifticon.application;

import com.mkhwang.gifticon.command.gifticon.application.command.GifticonCommand;

public interface GifticonCommandHandler {

    GifticonDto.Gifticon createGifticon(GifticonCommand.CreateGifticon command);

    GifticonDto.Gifticon tradeGifticon(GifticonCommand.TradeGifticon command);

    void deleteGifticon(GifticonCommand.DeleteGifticon command);
}