package com.mkhwang.trader.query.gifticon.application;

import com.mkhwang.trader.common.gifticon.domain.Gifticon;
import com.mkhwang.trader.common.gifticon.infra.GifticonRepository;
import com.mkhwang.trader.query.gifticon.application.mapper.GifticonDocumentMapper;
import com.mkhwang.trader.query.gifticon.domain.GifticonSearchDocument;
import com.mkhwang.trader.query.gifticon.infra.GifticonSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GifticonSearchDocumentServiceImpl implements GifticonDocumentService {
  private final GifticonRepository gifticonRepository;
  private final GifticonSearchRepository gifticonSearchRepository;
  private final GifticonDocumentMapper gifticonDocumentMapper;


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
              .map(gifticonDocumentMapper::toSearchDocument)
              .toList();

      gifticonSearchRepository.saveAll(documents); // Bulk 저장
      page++;
    } while (!gifticons.isLast());
  }
}
