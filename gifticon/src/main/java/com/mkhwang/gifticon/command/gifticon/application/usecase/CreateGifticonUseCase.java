package com.mkhwang.gifticon.command.gifticon.application.usecase;

import com.mkhwang.gifticon.command.gifticon.application.command.GifticonCommand;
import com.mkhwang.gifticon.command.gifticon.presentation.dto.GifticonDto;

public interface CreateGifticonUseCase {
  GifticonDto.Gifticon createGifticon(GifticonCommand.CreateGifticon command);


}
