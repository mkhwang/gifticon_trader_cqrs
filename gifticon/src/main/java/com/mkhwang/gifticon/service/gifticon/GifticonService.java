package com.mkhwang.gifticon.service.gifticon;

import com.mkhwang.gifticon.config.GenericMapper;
import com.mkhwang.gifticon.controller.mapper.GifticonMapper;
import com.mkhwang.gifticon.exception.ResourceNotFoundException;
import com.mkhwang.gifticon.repository.*;
import com.mkhwang.gifticon.service.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Component
@RequiredArgsConstructor
public class GifticonService implements GifticonCommandHandler {
  private final GifticonRepository gifticonRepository;
  private final UserRepository userRepository;
  private final BrandRepository brandRepository;
  private final CategoryRepository categoryRepository;
  private final GifticonMapper gifticonMapper;
  private final TagRepository tagRepository;

  @Override
  @Transactional
  public GifticonDto.Gifticon createProduct(GifticonCommand.CreateGifticon command) {
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

    // 최종 저장 및 응답 생성
    gifticon = gifticonRepository.save(gifticon);
    return gifticonMapper.toDto(gifticon);
  }


}
