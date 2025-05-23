package com.mkhwang.gifticon.service.query.sync;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkhwang.gifticon.repository.GifticonRepository;
import com.mkhwang.gifticon.service.entity.Gifticon;
import com.mkhwang.gifticon.service.query.entity.GifticonDocument;
import com.mkhwang.gifticon.service.query.repository.GifticonDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GifticonDocumentServiceImpl implements GifticonDocumentService {
  private final GifticonRepository gifticonRepository;
  private final GifticonDocumentRepository gifticonDocumentRepository;
  private final ObjectMapper objectMapper;


  @Override
  public String getInfra() {
    return "mongodb";
  }

  @Override
  @Transactional(readOnly = true)
  public void syncGifticonAll() {
    int page = 0;
    int size = 1000;

    Page<Gifticon> gifticons;
    do {
      gifticons = gifticonRepository.findAll(PageRequest.of(page, size));
      List<GifticonDocument> documents = gifticons.getContent().stream()
              .map(this::convertToDocument)
              .toList();

      gifticonDocumentRepository.saveAll(documents); // Bulk 저장
      page++;
    } while (!gifticons.isLast());
  }

  private GifticonDocument convertToDocument(Gifticon gifticon) {
    return GifticonDocument.builder()
            .id(gifticon.getId())
            .name(gifticon.getName())
            .build();
  }
}
