package com.mkhwang.gifticon.service.gifticon;

public interface GifticonCommandHandler {

    GifticonDto.Gifticon createProduct(GifticonCommand.CreateGifticon command);
}