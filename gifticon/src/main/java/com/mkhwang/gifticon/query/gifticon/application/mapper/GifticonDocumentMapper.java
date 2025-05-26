package com.mkhwang.gifticon.query.gifticon.application.mapper;

import com.mkhwang.gifticon.command.gifticon.domain.Gifticon;
import com.mkhwang.gifticon.command.gifticon.presentation.dto.GifticonDto;
import com.mkhwang.gifticon.command.tag.domain.Tag;
import com.mkhwang.gifticon.query.gifticon.domain.GifticonDocument;
import com.mkhwang.gifticon.query.gifticon.domain.GifticonSearchDocument;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Component
public class GifticonDocumentMapper {
  public GifticonSearchDocument toSearchDocument(Gifticon gifticon) {
    return GifticonSearchDocument.builder()
            .id(gifticon.getId())
            .name(gifticon.getName())
            .description(gifticon.getDescription())
            .status(gifticon.getStatus().toString())
            .basePrice(gifticon.getPrice().getBasePrice())
            .salePrice(gifticon.getPrice().getSalePrice())
            .category(gifticon.getCategory().getName())
            .seller(gifticon.getSeller().getNickname())
            .sellerId(gifticon.getSeller().getId())
            .brand(gifticon.getBrand().getName())
            .tags(gifticon.getTags().stream()
                    .map(Tag::getName)
                    .toList())
            .createdAt(gifticon.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant())
            .updatedAt(gifticon.getUpdatedAt().atZone(ZoneId.systemDefault()).toInstant())
            .slug(gifticon.getSlug())
            .build();

  }

  public GifticonDocument toDocument(Gifticon gifticon) {
    return GifticonDocument.builder()
            .id(gifticon.getId())
            .name(gifticon.getName())
            .description(gifticon.getDescription())
            .status(gifticon.getStatus().toString())
            .price(
                    Map.of(
                            "basePrice", gifticon.getPrice().getBasePrice(),
                            "salePrice", gifticon.getPrice().getSalePrice(),
                            "currency", gifticon.getPrice().getCurrency(),
                            "discountPercentage", gifticon.getPrice().getDiscountPercentage()
                    )
            )
            .category(
                    Map.of(
                            "id", gifticon.getCategory().getId(),
                            "name", gifticon.getCategory().getName(),
                            "slug", gifticon.getCategory().getSlug()
                    )
            )
            .seller(
                    Map.of(
                            "id", gifticon.getSeller().getId(),
                            "nickname", gifticon.getSeller().getNickname()
                    )
            )
            .brand(
                    Map.of(
                            "id", gifticon.getBrand().getId(),
                            "name", gifticon.getBrand().getName()
                    )
            )
            .tags(
                    gifticon.getTags().stream()
                            .map(tag -> {
                              Map<String, Object> tagMap = new HashMap<>();
                              tagMap.put("id", tag.getId());
                              tagMap.put("name", tag.getName());
                              tagMap.put("slug", tag.getSlug());
                              return tagMap;
                            }).toList()
            )
            .createdAt(gifticon.getCreatedAt())
            .updatedAt(gifticon.getUpdatedAt())
            .slug(gifticon.getSlug())
            .build();
  }

  public GifticonDto.GifticonSummary toSummary(GifticonDocument gifticonDocument) {
    return GifticonDto.GifticonSummary.builder().build();
  }
}
