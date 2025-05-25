package com.mkhwang.gifticon.command.gifticon.application.usecase;

import com.mkhwang.gifticon.command.gifticon.application.command.GifticonCommand;
import com.mkhwang.gifticon.command.gifticon.presentation.dto.GifticonDto;

public interface DeleteGifticonUseCase {

  void deleteGifticon(GifticonCommand.DeleteGifticon command);
}
