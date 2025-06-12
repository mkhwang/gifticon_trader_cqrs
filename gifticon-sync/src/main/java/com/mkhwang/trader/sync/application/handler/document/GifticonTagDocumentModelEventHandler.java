package com.mkhwang.trader.sync.application.handler.document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkhwang.trader.common.tag.infra.TagRepository;
import com.mkhwang.trader.query.gifticon.domain.GifticonDocument;
import com.mkhwang.trader.query.gifticon.infra.GifticonDocumentRepository;
import com.mkhwang.trader.sync.application.handler.dto.CdcEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class GifticonTagDocumentModelEventHandler extends GifticonDocumentModelBaseEventHandler {

  private final GifticonDocumentRepository gifticonDocumentRepository;
  private final TagRepository tagRepository;

  public GifticonTagDocumentModelEventHandler(ObjectMapper objectMapper, GifticonDocumentRepository gifticonDocumentRepository,
                                              TagRepository tagRepository) {
    super(objectMapper);
    this.gifticonDocumentRepository = gifticonDocumentRepository;
    this.tagRepository = tagRepository;
  }

  @Override
  protected String getSupportedTable() {
    return "gifticon_tags";
  }

  @Override
  public void handle(CdcEvent event) {
    Map<String, Object> data;
    Long gifticonId;

    if (event.isDelete()) {
      data = event.getBeforeData();
    } else {
      data = event.getAfterData();
    }

    if (data == null || !data.containsKey("gifticon_id") || !data.containsKey("tag_id")) {
      return;
    }

    gifticonId = getLongValue(data, "gifticon_id");
    Long tagId = getLongValue(data, "tag_id");

    Optional<GifticonDocument> optionalDocument = gifticonDocumentRepository.findById(gifticonId);

    if (optionalDocument.isEmpty()) {
      log.warn("Gifticon document not found for tag update: {}", gifticonId);
      return;
    }

    GifticonDocument document = optionalDocument.get();
    List<Map<String, Object>> tags = document.getTags();

    // 삭제 이벤트 처리
    if (event.isDelete()) {
      tags.removeIf(tag -> tag.get("id").equals(tagId));
      gifticonDocumentRepository.save(document);
      return;
    }

    // 이미 존재하는 태그인지 확인
    boolean tagExists = tags.stream()
            .anyMatch(tag -> tag.get("id").equals(tagId));

    // 존재하지 않는 경우에만 추가
    if (!tagExists) {
      tagRepository.findById(tagId).ifPresent(
              tag -> {
                Map<String, Object> tagInfo = Map.of(
                        "id", tag.getId(),
                        "name", tag.getName(),
                        "slug", tag.getSlug()
                );
                tags.add(tagInfo);
                gifticonDocumentRepository.save(document);
              }
      );
    }

    log.info("Updated gifticon tag mapping for gifticon ID: {}, tag ID: {}", gifticonId, tagId);
  }
}