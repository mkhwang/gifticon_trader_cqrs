package com.mkhwang.trader.command.gifticon.application.usecase;

import com.mkhwang.trader.command.gifticon.application.command.GifticonCommand;

public interface DeleteGifticonUseCase {

  void deleteGifticon(GifticonCommand.DeleteGifticon command);
}
