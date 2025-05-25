package com.mkhwang.gifticon.query.gifticon.application;


import com.mkhwang.gifticon.command.gifticon.presentation.dto.GifticonDto;
import com.mkhwang.gifticon.command.gifticon.presentation.dto.GifticonListResponse;
import com.mkhwang.gifticon.query.gifticon.presentation.dto.GifticonQuery;

public interface GifticonQueryHandler {
  // 상품 조회
  GifticonDto.Gifticon getGifticon(GifticonQuery.GetGifticon query);

  GifticonListResponse getGifticons(GifticonQuery.ListGifticons query);
}
