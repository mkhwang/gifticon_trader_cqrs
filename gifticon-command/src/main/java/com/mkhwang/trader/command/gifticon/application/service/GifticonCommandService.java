package com.mkhwang.trader.command.gifticon.application.service;

import com.mkhwang.trader.command.gifticon.application.command.GifticonCommand;
import com.mkhwang.trader.command.gifticon.application.mapper.GifticonCommandResponseMapper;
import com.mkhwang.trader.command.gifticon.application.usecase.CreateGifticonUseCase;
import com.mkhwang.trader.command.gifticon.application.usecase.DeleteGifticonUseCase;
import com.mkhwang.trader.command.gifticon.application.usecase.TradeGifticonUseCase;
import com.mkhwang.trader.common.brand.domain.Brand;
import com.mkhwang.trader.common.brand.infra.BrandRepository;
import com.mkhwang.trader.common.category.domain.Category;
import com.mkhwang.trader.common.category.infra.CategoryRepository;
import com.mkhwang.trader.common.exception.ResourceNotFoundException;
import com.mkhwang.trader.common.gifticon.domain.Gifticon;
import com.mkhwang.trader.common.gifticon.domain.GifticonImage;
import com.mkhwang.trader.common.gifticon.domain.GifticonPrice;
import com.mkhwang.trader.common.gifticon.domain.GifticonStatus;
import com.mkhwang.trader.common.gifticon.infra.GifticonRepository;
import com.mkhwang.trader.command.gifticon.presentation.dto.GifticonDto;
import com.mkhwang.trader.common.tag.domain.Tag;
import com.mkhwang.trader.common.tag.infra.TagRepository;
import com.mkhwang.trader.common.user.domain.User;
import com.mkhwang.trader.common.user.infra.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Component
@RequiredArgsConstructor
public class GifticonCommandService implements CreateGifticonUseCase, DeleteGifticonUseCase, TradeGifticonUseCase {
  private final GifticonRepository gifticonRepository;
  private final UserRepository userRepository;
  private final BrandRepository brandRepository;
  private final CategoryRepository categoryRepository;
  private final GifticonCommandResponseMapper gifticonCommandResponseMapper;
  private final TagRepository tagRepository;

  @Override
  @Transactional
  public GifticonDto.Gifticon createGifticon(GifticonCommand.CreateGifticon command) {
    Gifticon gifticon = Gifticon.builder()
            .name(command.getName())
            .slug(command.getSlug())
            .description(command.getDescription())
            .status(GifticonStatus.ON_SALE)
            .build();

    // 2. 연관 엔티티 설정
    if (command.getSellerId() != null) {
      User seller = userRepository.findById(command.getSellerId())
              .orElseThrow(() -> new ResourceNotFoundException("User", command.getSellerId()));
      gifticon.setSeller(seller);
    }

    if (command.getBrandId() != null) {
      Brand brand = brandRepository.findById(command.getBrandId())
              .orElseThrow(() -> new ResourceNotFoundException("Brand", command.getBrandId()));
      gifticon.setBrand(brand);
    }

    // 3. 저장 및 ID 획득
    gifticon = gifticonRepository.save(gifticon);


    // Price 생성 및 저장
    if (command.getPrice() != null) {
      GifticonPrice price = GifticonPrice.builder()
              .gifticon(gifticon)
              .basePrice(command.getPrice().getBasePrice())
              .salePrice(command.getPrice().getSalePrice())
              .currency(command.getPrice().getCurrency())
              .build();
      gifticon.setPrice(price);
    }

    // 카테고리 연결
    Category category = categoryRepository.findById(command.getCategoryId()).orElseThrow(
            () -> new ResourceNotFoundException("Category", command.getCategoryId()));
    gifticon.setCategory(category);


    // 태그 연결
    if (command.getTagIds() != null && !command.getTagIds().isEmpty()) {
      List<Tag> tags = tagRepository.findAllById(command.getTagIds());
      gifticon.getTags().addAll(tags);
    }


    // 이미지 생성
    if (command.getImages() != null) {
      for (GifticonDto.Image imageDto : command.getImages()) {
        GifticonImage image = GifticonImage.builder()
                .url(imageDto.getUrl())
                .altText(imageDto.getAltText())
                .isPrimary(imageDto.isPrimary())
                .displayOrder(imageDto.getDisplayOrder())
                .build();
        gifticon.getImages().add(image);
      }
    }

    gifticon = gifticonRepository.save(gifticon);
    return gifticonCommandResponseMapper.toDto(gifticon);
  }

  @Override
  @Transactional
  public GifticonDto.Gifticon tradeGifticon(GifticonCommand.TradeGifticon command) {
    Gifticon gifticon = gifticonRepository.findById(command.getGifticonId()).orElseThrow(
            () -> new ResourceNotFoundException("Gifticon", command.getGifticonId()));
    User user = userRepository.findById(command.getBuyer()).orElseThrow(
            () -> new ResourceNotFoundException("User", command.getBuyer()));

    gifticon.buy(user);
    gifticon = gifticonRepository.save(gifticon);
    return gifticonCommandResponseMapper.toDto(gifticon);
  }

  @Override
  @Transactional
  public void deleteGifticon(GifticonCommand.DeleteGifticon command) {
    Gifticon gifticon = gifticonRepository.findById(command.getGifticonId()).orElseThrow(
            () -> new ResourceNotFoundException("Gifticon", command.getGifticonId()));

    gifticon.delete(command.getUserId());
    gifticonRepository.save(gifticon);
  }
}
