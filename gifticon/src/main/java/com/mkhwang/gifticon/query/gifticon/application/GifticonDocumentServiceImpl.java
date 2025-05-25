package com.mkhwang.gifticon.query.gifticon.application;

import com.mkhwang.gifticon.command.gifticon.domain.Gifticon;
import com.mkhwang.gifticon.command.gifticon.infra.GifticonRepository;
import com.mkhwang.gifticon.query.gifticon.application.mapper.GifticonDocumentMapper;
import com.mkhwang.gifticon.query.gifticon.domain.GifticonDocument;
import com.mkhwang.gifticon.query.gifticon.infra.GifticonDocumentRepository;
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
  private final GifticonDocumentMapper gifticonDocumentMapper;


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
              .map(gifticonDocumentMapper::toDocument)
              .toList();

      gifticonDocumentRepository.saveAll(documents); // Bulk 저장
      page++;
    } while (!gifticons.isLast());
  }
}
