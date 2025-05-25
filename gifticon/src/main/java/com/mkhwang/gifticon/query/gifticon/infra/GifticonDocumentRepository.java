package com.mkhwang.gifticon.query.gifticon.infra;

import com.mkhwang.gifticon.query.gifticon.domain.GifticonDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GifticonDocumentRepository extends MongoRepository<GifticonDocument, Long> {
}
