package com.mkhwang.gifticon.service.gifticon;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class GifticonCommand {
  @Data
  @Builder
  public static class CreateGifticon {
    private String name;
    private String slug;
    private String description;
    private Long sellerId;
    private Long brandId;
    private Long categoryId;
    private String status;

    private GifticonDto.Price price;
    @Builder.Default
    private List<GifticonDto.Image> images = new ArrayList<>();
    @Builder.Default
    private List<Long> tagIds = new ArrayList<>();
  }
}
