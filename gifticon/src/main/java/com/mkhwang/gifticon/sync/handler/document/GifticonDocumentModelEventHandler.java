package com.mkhwang.gifticon.sync.handler.document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkhwang.gifticon.query.brand.infra.BrandRepository;
import com.mkhwang.gifticon.query.gifticon.domain.GifticonDocument;
import com.mkhwang.gifticon.query.gifticon.infra.GifticonDocumentRepository;
import com.mkhwang.gifticon.query.user.infra.UserRepository;
import com.mkhwang.gifticon.sync.handler.dto.CdcEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class GifticonDocumentModelEventHandler extends GifticonDocumentModelBaseEventHandler {

    private final GifticonDocumentRepository gifticonDocumentRepository;
    private final BrandRepository brandRepository;
    private final UserRepository userRepository;

    public GifticonDocumentModelEventHandler(ObjectMapper objectMapper, GifticonDocumentRepository gifticonDocumentRepository,
                                             BrandRepository brandRepository, UserRepository userRepository) {
        super(objectMapper);
        this.gifticonDocumentRepository = gifticonDocumentRepository;
        this.brandRepository = brandRepository;
        this.userRepository = userRepository;
    }

    @Override
    protected String getSupportedTable() {
        return "gifticons";
    }

    @Override
    public void handle(CdcEvent event) {
        if (event.isDelete()) {
            handleDelete(event);
        } else {
            handleCreateOrUpdate(event);
        }
    }

    private void handleDelete(CdcEvent event) {
        Map<String, Object> data = event.getBeforeData();
        if (data == null || !data.containsKey("id")) {
            return;
        }

        Long gifticonId = getLongValue(data, "id");
        gifticonDocumentRepository.deleteById(gifticonId);
        log.info("Deleted gifticon document: {}", gifticonId);
    }

    private void handleCreateOrUpdate(CdcEvent event) {
        // 기존 로직 구현
        Map<String, Object> data = event.getAfterData();
        if (data == null || !data.containsKey("id")) {
            return;
        }

        Long gifticonId = getLongValue(data, "id");

        // 기존 문서 조회 또는 신규 생성
        GifticonDocument document = gifticonDocumentRepository.findById(gifticonId)
                .orElse(GifticonDocument.builder().id(gifticonId).build());

        // 기본 정보 업데이트
        document.setName(getStringValue(data, "name"));
        document.setSlug(getStringValue(data, "slug"));
        document.setDescription(getStringValue(data, "description"));
        document.setStatus(getStringValue(data, "status"));

        // 날짜 필드 처리
        if (data.containsKey("created_at")) {
            document.setCreatedAt(parseTimestampToLocalDateTime(data.get("created_at")));
        }

        if (data.containsKey("updated_at")) {
            document.setUpdatedAt(parseTimestampToLocalDateTime(data.get("updated_at")));
        }

        // 판매자 정보 설정
        if (data.containsKey("seller_id") && data.get("seller_id") != null) {
            Long sellerId = getLongValue(data, "seller_id");
            if (document.getSeller() == null) {
                userRepository.findById(sellerId).ifPresent(
                        seller -> document.setSeller(Map.of(
                                "id", seller.getId(),
                                "nickName", seller.getNickname()
                        ))
                );
            }
        }

        // 브랜드 정보 설정
        if (data.containsKey("brand_id") && data.get("brand_id") != null) {
            Long brandId = getLongValue(data, "brand_id");
            if (document.getBrand() == null) {
                brandRepository.findById(brandId).ifPresent(
                        brand -> document.setBrand(Map.of(
                                "id", brand.getId(),
                                "name", brand.getName()
                        ))
                );
            }
        }

        // 저장
        gifticonDocumentRepository.save(document);
        log.info("Saved gifticon document: {}", gifticonId);
    }
}