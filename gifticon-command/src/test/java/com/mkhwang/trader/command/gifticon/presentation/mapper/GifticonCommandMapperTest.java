package com.mkhwang.trader.command.gifticon.presentation.mapper;

import com.mkhwang.trader.command.gifticon.application.command.GifticonCommand;
import com.mkhwang.trader.command.gifticon.presentation.dto.GifticonCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;


class GifticonCommandMapperTest {

  private final GifticonCommandMapper mapper = new GifticonCommandMapper();

  @DisplayName("Gifticon Create Command 변환 테스트")
  @Test
  void toCreateCommand() {
    // given

    GifticonCreateRequest request = new GifticonCreateRequest();
    request.setBrandId(1L);
    request.setCategoryId(2L);
    request.setDescription("기프티콘 설명");
    request.setName("스타벅스 아메리카노");
    request.setSlug("starbucks");
    request.setSellerId(99L);
    request.setPrice(new GifticonCreateRequest.GifticonPriceDto());
    request.getPrice().setBasePrice(BigDecimal.valueOf(10000));
    request.getPrice().setSalePrice(BigDecimal.valueOf(9000));
    request.getPrice().setCurrency("KRW");

    // when
    GifticonCommand.CreateGifticon command = mapper.toCreateCommand(request);

    // then
    assertEquals(1L, command.getBrandId());
    assertEquals(2L, command.getCategoryId());
    assertEquals("기프티콘 설명", command.getDescription());
    assertEquals("스타벅스 아메리카노", command.getName());
    assertEquals("starbucks", command.getSlug());
    assertEquals(99L, command.getSellerId());

    assertNotNull(command.getPrice());
    assertEquals(BigDecimal.valueOf(10000), command.getPrice().getBasePrice());
    assertEquals(BigDecimal.valueOf(9000), command.getPrice().getSalePrice());
    assertEquals("KRW", command.getPrice().getCurrency());
  }

  @DisplayName("Gifticon Trade Command 변환 테스트")
  @Test
  void toTradeCommand() {
    // given
    Long id = 1L;
    Long buyerId = 2L;

    // when
    GifticonCommand.TradeGifticon tradeCommand = mapper.toTradeCommand(id, buyerId);

    // then
    assertEquals(id, tradeCommand.getGifticonId());
    assertEquals(buyerId, tradeCommand.getBuyer());
  }

  @DisplayName("Gifticon Delete Command 변환 테스트")
  @Test
  void toDeleteCommand() {
    // given
    Long id = 1L;

    // when
    GifticonCommand.DeleteGifticon deleteCommand = mapper.toDeleteCommand(id);

    // then
    assertEquals(id, deleteCommand.getGifticonId());

  }
}