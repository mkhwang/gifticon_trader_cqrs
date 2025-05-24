package com.mkhwang.gifticon.service.query.repository;

import com.mkhwang.gifticon.service.query.entity.GifticonDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GifticonDocumentRepository extends MongoRepository<GifticonDocument, Long> {
}
