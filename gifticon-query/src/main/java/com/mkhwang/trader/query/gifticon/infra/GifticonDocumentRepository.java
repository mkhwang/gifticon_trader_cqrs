package com.mkhwang.trader.query.gifticon.infra;

import com.mkhwang.trader.query.gifticon.domain.GifticonDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GifticonDocumentRepository extends MongoRepository<GifticonDocument, Long> {
  List<GifticonDocument> findByIdIn(List<Long> gifticonIds);
}
