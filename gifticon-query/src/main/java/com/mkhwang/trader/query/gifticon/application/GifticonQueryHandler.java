package com.mkhwang.trader.query.gifticon.application;


import com.mkhwang.trader.query.gifticon.application.query.GifticonQuery;
import com.mkhwang.trader.query.gifticon.presentation.dto.GifticonListResponse;
import com.mkhwang.trader.query.gifticon.application.dto.GifticonQueryDto;

public interface GifticonQueryHandler {
  // 상품 조회
  GifticonQueryDto.Gifticon getGifticon(GifticonQuery.GetGifticon query);

  GifticonListResponse getGifticons(GifticonQuery.ListGifticons query);
}
