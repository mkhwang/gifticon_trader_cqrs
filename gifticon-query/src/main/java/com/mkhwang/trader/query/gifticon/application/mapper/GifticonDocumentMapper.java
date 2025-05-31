package com.mkhwang.trader.query.gifticon.application.mapper;

import com.mkhwang.trader.common.gifticon.domain.Gifticon;
import com.mkhwang.trader.common.tag.domain.Tag;
import com.mkhwang.trader.query.gifticon.application.dto.GifticonQueryDto;
import com.mkhwang.trader.query.gifticon.domain.GifticonDocument;
import com.mkhwang.trader.query.gifticon.domain.GifticonSearchDocument;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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

  public GifticonQueryDto.GifticonSummary toSummary(GifticonDocument gifticonDocument) {
    return GifticonQueryDto.GifticonSummary.builder()
            .id(gifticonDocument.getId())
            .name(gifticonDocument.getName())
            .description(gifticonDocument.getDescription())
            .status(gifticonDocument.getStatus())
            .basePrice(gifticonDocument.getPrice().get("basePrice") != null ? BigDecimal.valueOf(Double.parseDouble((String) gifticonDocument.getPrice().get("basePrice"))) : BigDecimal.ZERO)
            .salePrice(gifticonDocument.getPrice().get("salePrice") != null ? BigDecimal.valueOf(Double.parseDouble((String) gifticonDocument.getPrice().get("salePrice"))) : BigDecimal.ZERO)
            .currency(gifticonDocument.getPrice().get("currency") != null ? (String) gifticonDocument.getPrice().get("currency") : "KRW")
            .primaryImage(
                    gifticonDocument.getImages().stream().findFirst().filter(image -> image.get("primary").equals(Boolean.TRUE))
                            .map(image -> GifticonQueryDto.Image.builder()
                                    .id((Long) image.get("id"))
                                    .url((String) image.get("url"))
                                    .build())
                            .orElse(null)
            )
            .brand(
                    GifticonQueryDto.Brand.builder()
                            .id(gifticonDocument.getBrand().get("id") != null ? (Long) gifticonDocument.getBrand().get("id") : 0L)
                            .name(gifticonDocument.getBrand().get("name") != null ? (String) gifticonDocument.getBrand().get("name") : "")
                            .build()
            )
            .seller(
                    GifticonQueryDto.Seller.builder()
                            .id(gifticonDocument.getSeller().get("id") != null ? (Long) gifticonDocument.getSeller().get("id") : 0L)
                            .nickname(gifticonDocument.getSeller().get("nickname") != null ? (String) gifticonDocument.getSeller().get("nickname") : "")
                            .build()
            )
            .createdAt(gifticonDocument.getCreatedAt())
            .build();
  }
}
