package com.mkhwang.trader.command.gifticon.application.service;

import com.mkhwang.trader.command.gifticon.application.command.GifticonCommand;
import com.mkhwang.trader.command.gifticon.application.mapper.GifticonCommandResponseMapper;
import com.mkhwang.trader.common.brand.infra.BrandRepository;
import com.mkhwang.trader.common.category.infra.CategoryRepository;
import com.mkhwang.trader.common.gifticon.domain.Gifticon;
import com.mkhwang.trader.common.gifticon.infra.GifticonRepository;
import com.mkhwang.trader.common.tag.infra.TagRepository;
import com.mkhwang.trader.common.user.domain.User;
import com.mkhwang.trader.common.user.infra.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GifticonCommandServiceTest {
  @Mock
  GifticonRepository gifticonRepository;
  @Mock
  UserRepository userRepository;
  @Mock
  BrandRepository brandRepository;
  @Mock
  CategoryRepository categoryRepository;
  @Mock
  GifticonCommandResponseMapper gifticonCommandResponseMapper;
  @Mock
  TagRepository tagRepository;
  @InjectMocks
  GifticonCommandService gifticonCommandService;

  @DisplayName("createGifticon: Gifticon을 생성한다.")
  @Test
  void createGifticon() {
  }

  @DisplayName("tradeGifticon: Gifticon을 거래한다.")
  @Test
  void tradeGifticon() {
    // given
    Long existId = 1L;
    Long userId = 2L;
    GifticonCommand.TradeGifticon command = mock(GifticonCommand.TradeGifticon.class);
    Gifticon gifticon = mock(Gifticon.class);
    User user = mock(User.class);
    given(command.getGifticonId()).willReturn(existId);
    given(command.getBuyer()).willReturn(userId);
    given(gifticonRepository.findById(existId)).willReturn(Optional.of(gifticon));
    given(userRepository.findById(userId)).willReturn(Optional.of(user));

    // when
    gifticonCommandService.tradeGifticon(command);

    // then
    verify(gifticonRepository, times(1)).findById(existId);
    verify(userRepository, times(1)).findById(userId);
    verify(gifticon).buy(user);
    verify(gifticonRepository, times(1)).save(gifticon);
  }

  @DisplayName("deleteGifticon: Gifticon을 삭제한다.")
  @Test
  void deleteGifticon() {
    // given
    Long existId = 1L;
    GifticonCommand.DeleteGifticon command = mock(GifticonCommand.DeleteGifticon.class);
    Gifticon gifticon = mock(Gifticon.class);
    given(command.getGifticonId()).willReturn(existId);
    given(gifticonRepository.findById(existId)).willReturn(Optional.of(gifticon));

    // when
    gifticonCommandService.deleteGifticon(command);

    // then
    verify(gifticon).delete(command.getUserId());
    verify(gifticonRepository).save(gifticon);
  }
}