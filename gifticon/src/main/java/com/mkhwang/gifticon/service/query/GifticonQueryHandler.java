package com.mkhwang.gifticon.service.query;


import com.mkhwang.gifticon.controller.dto.GifticonListResponse;
import com.mkhwang.gifticon.service.gifticon.GifticonDto;

public interface GifticonQueryHandler {
    // 상품 조회
    GifticonDto.Gifticon getGifticon(GifticonQuery.GetGifticon query);

    GifticonListResponse getGifticons(GifticonQuery.ListGifticons query);
}
