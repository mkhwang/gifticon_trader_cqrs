package com.mkhwang.trader.command.gifticon.presentation.mapper;

import com.mkhwang.trader.command.gifticon.application.command.GifticonCommand;
import com.mkhwang.trader.command.gifticon.presentation.dto.GifticonCreateRequest;
import com.mkhwang.trader.command.gifticon.presentation.dto.GifticonDto;
import org.springframework.stereotype.Component;

@Component
public class GifticonCommandMapper {

  public GifticonCommand.CreateGifticon toCreateCommand(GifticonCreateRequest request) {
    return GifticonCommand.CreateGifticon.builder()
            .brandId(request.getBrandId())
            .categoryId(request.getCategoryId())
            .description(request.getDescription())
            .name(request.getName())
            .slug(request.getSlug())
            .sellerId(request.getSellerId())
            .price(GifticonDto.Price.builder()
                    .basePrice(request.getPrice().getBasePrice())
                    .salePrice(request.getPrice().getSalePrice())
                    .currency(request.getPrice().getCurrency())
                    .build())
            .build();
  }


  public GifticonCommand.TradeGifticon toTradeCommand(Long id, Long buyerId) {
    return GifticonCommand.TradeGifticon.builder()
            .gifticonId(id)
            .buyer(buyerId)
            .build();
  }

  public GifticonCommand.DeleteGifticon toDeleteCommand(Long id) {
    return GifticonCommand.DeleteGifticon.builder()
            .gifticonId(id)
            .build();
  }
}
