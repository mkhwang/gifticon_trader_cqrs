package com.mkhwang.gifticon.query.gifticon.application;

import com.mkhwang.gifticon.command.gifticon.presentation.dto.GifticonDto;
import com.mkhwang.gifticon.command.gifticon.presentation.dto.GifticonListResponse;
import com.mkhwang.gifticon.query.gifticon.application.mapper.GifticonQueryResponseMapper;
import com.mkhwang.gifticon.query.gifticon.domain.GifticonDocument;
import com.mkhwang.gifticon.query.gifticon.domain.UserRatingSummary;
import com.mkhwang.gifticon.query.gifticon.infra.GifticonDocumentRepository;
import com.mkhwang.gifticon.query.gifticon.presentation.dto.GifticonQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GifticonQueryService implements GifticonQueryHandler {
  private final GifticonDocumentRepository gifticonDocumentRepository;
  private final RedisTemplate<String, UserRatingSummary> userRatingSummaryRedisTemplate;
  private final GifticonQueryResponseMapper gifticonQueryResponseMapper;

  @Override
  public GifticonDto.Gifticon getGifticon(GifticonQuery.GetGifticon query) {
    GifticonDocument gifticonDocument = gifticonDocumentRepository.findById(query.getGifticonId())
            .orElseThrow(() -> new IllegalArgumentException("Gifticon not found with id: " + query.getGifticonId()));

    UserRatingSummary summary =
            Optional.ofNullable(userRatingSummaryRedisTemplate.opsForValue().get("user:summary:" + gifticonDocument.getSeller().get("id")))
                    .orElse(UserRatingSummary.builder()
                            .id(0L)
                            .averageRating(0.0)
                            .totalCount(0)
                            .build());

    return gifticonQueryResponseMapper.toGifticon(gifticonDocument, summary);
  }

  @Override
  public GifticonListResponse getGifticons(GifticonQuery.ListGifticons query) {
    return null;
  }
}
