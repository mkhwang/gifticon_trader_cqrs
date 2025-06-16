package com.mkhwang.trader.command.gifticon.application.service;

import com.mkhwang.trader.command.gifticon.application.command.GifticonCommand;
import com.mkhwang.trader.command.gifticon.application.mapper.GifticonCommandResponseMapper;
import com.mkhwang.trader.command.gifticon.presentation.dto.GifticonDto;
import com.mkhwang.trader.common.brand.domain.Brand;
import com.mkhwang.trader.common.brand.infra.BrandRepository;
import com.mkhwang.trader.common.category.domain.Category;
import com.mkhwang.trader.common.category.infra.CategoryRepository;
import com.mkhwang.trader.common.gifticon.domain.Gifticon;
import com.mkhwang.trader.common.gifticon.infra.GifticonRepository;
import com.mkhwang.trader.common.tag.domain.Tag;
import com.mkhwang.trader.common.tag.infra.TagRepository;
import com.mkhwang.trader.common.user.domain.User;
import com.mkhwang.trader.common.user.infra.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
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
    // Given
    GifticonCommand.CreateGifticon command = GifticonCommand.CreateGifticon.builder()
            .name("스타벅스 아메리카노")
            .slug("starbucks-americano")
            .description("테스트 설명")
            .sellerId(1L)
            .brandId(2L)
            .categoryId(3L)
            .price(GifticonDto.Price.builder().build())
            .tagIds(List.of(10L, 20L))
            .images(List.of(
                    GifticonDto.Image.builder().build(),
                    GifticonDto.Image.builder().build()
            ))
            .build();

    User mockSeller = mock(User.class);
    Brand mockBrand = mock(Brand.class);
    Category mockCategory = new Category();
    List<Tag> mockTags = List.of(mock(Tag.class));

    Gifticon savedGifticon = new Gifticon();
    GifticonDto.Gifticon dto = new GifticonDto.Gifticon();

    given(userRepository.findById(1L)).willReturn(Optional.of(mockSeller));
    given(brandRepository.findById(2L)).willReturn(Optional.of(mockBrand));
    given(categoryRepository.findById(3L)).willReturn(Optional.of(mockCategory));
    given(tagRepository.findAllById(command.getTagIds())).willReturn(mockTags);
    given(gifticonRepository.save(any(Gifticon.class))).willReturn(savedGifticon);
    given(gifticonCommandResponseMapper.toDto(savedGifticon)).willReturn(dto);

    // When
    GifticonDto.Gifticon result = gifticonCommandService.createGifticon(command);
    assertEquals(dto, result);

    // Then
    verify(userRepository).findById(1L);
    verify(brandRepository).findById(2L);
    verify(categoryRepository).findById(3L);
    verify(tagRepository).findAllById(command.getTagIds());
    verify(gifticonRepository, times(2)).save(any(Gifticon.class));
    verify(gifticonCommandResponseMapper).toDto(savedGifticon);
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