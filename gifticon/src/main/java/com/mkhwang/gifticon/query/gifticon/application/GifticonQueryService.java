package com.mkhwang.gifticon.query.gifticon.application;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.mkhwang.gifticon.command.gifticon.presentation.dto.GifticonDto;
import com.mkhwang.gifticon.command.gifticon.presentation.dto.GifticonListResponse;
import com.mkhwang.gifticon.common.dto.PaginationDto;
import com.mkhwang.gifticon.query.gifticon.application.mapper.GifticonDocumentMapper;
import com.mkhwang.gifticon.query.gifticon.application.mapper.GifticonQueryResponseMapper;
import com.mkhwang.gifticon.query.gifticon.application.search.SearchQueryBuilder;
import com.mkhwang.gifticon.query.gifticon.domain.GifticonDocument;
import com.mkhwang.gifticon.query.gifticon.domain.GifticonSearchDocument;
import com.mkhwang.gifticon.query.gifticon.domain.UserRatingSummary;
import com.mkhwang.gifticon.query.gifticon.infra.GifticonDocumentRepository;
import com.mkhwang.gifticon.query.gifticon.presentation.dto.GifticonQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GifticonQueryService implements GifticonQueryHandler {
  private final GifticonDocumentRepository gifticonDocumentRepository;
  private final RedisTemplate<String, UserRatingSummary> userRatingSummaryRedisTemplate;
  private final GifticonQueryResponseMapper gifticonQueryResponseMapper;
  private final SearchQueryBuilder searchQueryBuilder;
  private final ElasticsearchOperations elasticsearchOperations;
  private final GifticonDocumentMapper gifticonDocumentMapper;

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
    Pageable pageable = query.getPagination().toPageable();

    Query combined = searchQueryBuilder.build(query);

    NativeQuery searchQuery = NativeQuery.builder()
            .withQuery(combined)
            .withPageable(pageable)
            .build();

    SearchHits<GifticonSearchDocument> hits = elasticsearchOperations.search(
            searchQuery,
            GifticonSearchDocument.class
    );


    List<GifticonSearchDocument> result = hits.getSearchHits().stream()
            .map(SearchHit::getContent)
            .toList();


    List<Long> gifticonIds = result.stream().map(GifticonSearchDocument::getId).toList();
    return GifticonListResponse.builder().items(gifticonDocumentRepository.findByIdIn(gifticonIds).stream().map(
            gifticonDocumentMapper::toSummary
    ).toList()).pagination(
            PaginationDto.PaginationInfo.builder()
                    .currentPage(pageable.getPageNumber())
                    .perPage(pageable.getPageSize())
                    .totalItems((int) hits.getTotalHits())
                    .totalPages((int) (hits.getTotalHits() / pageable.getPageSize() + 1))
            .build()
    ).build();
  }
}
