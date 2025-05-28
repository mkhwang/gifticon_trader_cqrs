package com.mkhwang.trader.command.gifticon.application.usecase;

import com.mkhwang.trader.command.gifticon.application.command.GifticonCommand;
import com.mkhwang.trader.command.gifticon.presentation.dto.GifticonDto;

public interface CreateGifticonUseCase {
  GifticonDto.Gifticon createGifticon(GifticonCommand.CreateGifticon command);


}
