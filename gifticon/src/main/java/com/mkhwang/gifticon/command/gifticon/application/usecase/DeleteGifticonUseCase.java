package com.mkhwang.gifticon.command.gifticon.application.usecase;

import com.mkhwang.gifticon.command.gifticon.application.command.GifticonCommand;

public interface DeleteGifticonUseCase {

  void deleteGifticon(GifticonCommand.DeleteGifticon command);
}
