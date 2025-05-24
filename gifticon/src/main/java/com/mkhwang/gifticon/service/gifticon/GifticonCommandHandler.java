package com.mkhwang.gifticon.service.gifticon;

public interface GifticonCommandHandler {

    GifticonDto.Gifticon createGifticon(GifticonCommand.CreateGifticon command);

    GifticonDto.Gifticon tradeGifticon(GifticonCommand.TradeGifticon command);

    void deleteGifticon(GifticonCommand.DeleteGifticon command);
}