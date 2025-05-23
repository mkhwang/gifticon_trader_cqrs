package com.mkhwang.gifticon.service.query.sync.handler.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkhwang.gifticon.service.query.entity.GifticonSearchDocument;
import com.mkhwang.gifticon.service.query.repository.GifticonSearchRepository;
import com.mkhwang.gifticon.service.query.sync.CdcEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class GifticonTagSearchModelEventHandler extends GifticonSearchModelBaseEventHandler {

    private final GifticonSearchRepository gifticonSearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public GifticonTagSearchModelEventHandler(
            ObjectMapper objectMapper,
            GifticonSearchRepository gifticonSearchRepository,
            ElasticsearchOperations elasticsearchOperations) {
        super(objectMapper);
        this.gifticonSearchRepository = gifticonSearchRepository;
        this.elasticsearchOperations = elasticsearchOperations;
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

        // 태그 목록을 조회해야 함 - 기존 문서 필요
        Optional<GifticonSearchDocument> optionalDocument = gifticonSearchRepository.findById(gifticonId);
        if (optionalDocument.isEmpty()) {
            log.warn("Gifticon document not found for tag update: {}", gifticonId);
            return;
        }

        GifticonSearchDocument document = optionalDocument.get();

        // 기존 태그 목록 가져오기
        List<Long> tagIds = document.getTagIds();
        if (tagIds == null) {
            tagIds = new ArrayList<>();
        }

        // 태그 매핑 추가 또는 제거
        boolean updated = false;
        if (event.isDelete()) {
            updated = tagIds.remove(tagId);
        } else if (!tagIds.contains(tagId)) {
            tagIds.add(tagId);
            updated = true;
        }

        // 변경된 경우에만 업데이트
        if (updated) {
            Map<String, Object> updates = new HashMap<>();
            updates.put("tagIds", tagIds);
            updatePartialDocument(gifticonId, updates);
        }
    }

    // 부분 업데이트를 위한 메서드
    private void updatePartialDocument(Long gifticonId, Map<String, Object> updates) {
        if (gifticonId == null || updates == null || updates.isEmpty()) {
            return;
        }

        try {
            // Document 객체 생성
            Document document = Document.create();

            // 각 필드를 Document에 추가
            updates.forEach(document::put);

            UpdateQuery updateQuery = UpdateQuery.builder(gifticonId.toString())
                    .withDocument(document)
                    .withDocAsUpsert(true) // 문서가 없으면 생성
                    .build();

            elasticsearchOperations.update(updateQuery, IndexCoordinates.of("gifticons"));
            log.debug("Partially updated gifticon tags: {}", gifticonId);
        } catch (Exception e) {
            log.error("Error updating gifticon tags {}: {}", gifticonId, e.getMessage());
        }
    }
}