package com.mkhwang.gifticon.sync.handler.document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkhwang.gifticon.query.gifticon.domain.GifticonDocument;
import com.mkhwang.gifticon.query.gifticon.infra.GifticonDocumentRepository;
import com.mkhwang.gifticon.sync.handler.dto.CdcEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class GifticonImageDocumentModelEventHandler extends GifticonDocumentModelBaseEventHandler {

    private final GifticonDocumentRepository gifticonDocumentRepository;

    public GifticonImageDocumentModelEventHandler(
            ObjectMapper objectMapper,
            GifticonDocumentRepository gifticonDocumentRepository) {
        super(objectMapper);
        this.gifticonDocumentRepository = gifticonDocumentRepository;
    }

    @Override
    protected String getSupportedTable() {
        return "gifticon_images";
    }

    @Override
    public void handle(CdcEvent event) {
        Map<String, Object> data;
        Long imageId;
        Long gifticonId = 0L;

        if (event.isDelete()) {
            data = event.getBeforeData();
        } else {
            data = event.getAfterData();
        }

        if (data == null) {
            return;
        }

        imageId = getLongValue(data, "id");

        // 상품 ID 직접 존재하는 경우
        if (data.containsKey("gifticon_id")) {
            gifticonId = getLongValue(data, "gifticon_id");
        }

        if (gifticonId == null || gifticonId == 0L) {
            return;
        }

        // 상품 문서 조회
        Optional<GifticonDocument> optionalDocument = gifticonDocumentRepository.findById(gifticonId);
        if (optionalDocument.isEmpty()) {
            log.warn("gifticon document not found for image update: {}", gifticonId);
            return;
        }

        GifticonDocument document = optionalDocument.get();

        // 삭제 이벤트 처리
        if (event.isDelete()) {
            document.getImages().removeIf(img -> img.get("id").equals(imageId));

            gifticonDocumentRepository.save(document);
            log.info("Removed image {} from gifticon {}", imageId, gifticonId);
            return;
        }

        Map<String, Object> image = new HashMap<>();
        image.put("id", imageId);
        image.put("url", getStringValue(data, "url"));
        image.put("altText", getStringValue(data, "alt_text"));
        image.put("isPrimary", getBooleanValue(data, "is_primary") != null ? getBooleanValue(data, "is_primary") : false);
        image.put("displayOrder", getIntegerValue(data, "display_order"));


        // 이미지 목록 초기화 확인
        if (document.getImages() == null) {
            document.setImages(new ArrayList<>());
        }

        // 기존 이미지 찾기
        Optional<Map<String, Object>> existingImage = document.getImages().stream()
                .filter(img -> img.get("id").equals(imageId))
                .findFirst();

        if (existingImage.isPresent()) {
            // 기존 이미지 업데이트
            int index = document.getImages().indexOf(existingImage.get());
            document.getImages().set(index, image);
        } else {
            // 새 이미지 추가
            document.getImages().add(image);
        }

        gifticonDocumentRepository.save(document);
        log.info("Updated image for gifticon ID: {}, image ID: {}", gifticonId, imageId);
    }
}