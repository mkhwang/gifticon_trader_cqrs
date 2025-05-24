package com.mkhwang.gifticon.service.query.sync;

import com.mkhwang.gifticon.repository.GifticonRepository;
import com.mkhwang.gifticon.service.entity.Gifticon;
import com.mkhwang.gifticon.service.entity.Tag;
import com.mkhwang.gifticon.service.query.entity.GifticonSearchDocument;
import com.mkhwang.gifticon.service.query.repository.GifticonSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GifticonSearchDocumentServiceImpl implements GifticonDocumentService {
  private final GifticonRepository gifticonRepository;
  private final GifticonSearchRepository gifticonSearchRepository;


  @Override
  public String getInfra() {
    return "elasticsearch";
  }

  @Override
  @Transactional(readOnly = true)
  public void syncGifticonAll() {
    int page = 0;
    int size = 1000;

    Page<Gifticon> gifticons;
    do {
      gifticons = gifticonRepository.findAll(PageRequest.of(page, size));
      List<GifticonSearchDocument> documents = gifticons.getContent().stream()
              .map(this::convertToSearchDocument)
              .toList();

      gifticonSearchRepository.saveAll(documents); // Bulk 저장
      page++;
    } while (!gifticons.isLast());
  }

  private GifticonSearchDocument convertToSearchDocument(Gifticon gifticon) {
    return GifticonSearchDocument.builder()
            .id(gifticon.getId())
            .name(gifticon.getName())
            .description(gifticon.getDescription())
            .status(gifticon.getStatus().toString())
            .basePrice(gifticon.getPrice().getBasePrice())
            .salePrice(gifticon.getPrice().getSalePrice())
            .categoryId(gifticon.getCategory().getId())
            .sellerId(gifticon.getSeller().getId())
            .brandId(gifticon.getBrand().getId())
            .tagIds(gifticon.getTags().stream()
                    .map(Tag::getId)
                    .toList())
            .createdAt(gifticon.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant())
            .updatedAt(gifticon.getUpdatedAt().atZone(ZoneId.systemDefault()).toInstant())
            .slug(gifticon.getSlug())
            .build();
  }

}
