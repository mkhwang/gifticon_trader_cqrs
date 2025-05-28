package com.mkhwang.trader.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class PaginationRequest {
    private int page;
    private int size;
    private String sort = "createdAt:desc";

    static Sort createBasicSortBySortParams(String sort) {
      String[] sortParams = sort.split(":");
      String sortField = sortParams[0];
      Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc")
              ? Sort.Direction.ASC : Sort.Direction.DESC;
      return Sort.by(direction, sortField);
    }

    public Pageable toPageable() {
      return PageRequest.of(
              page - 1,
              size,
              createBasicSortBySortParams(sort)
      );
    }
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class PaginationInfo {
    private Integer totalItems;
    private Integer totalPages;
    private Integer currentPage;
    private Integer perPage;

    public static PaginationInfo empty(Pageable pageable) {
      return PaginationInfo.builder()
              .totalItems(0)
              .totalPages(0)
              .currentPage(pageable.getPageNumber() + 1) // 0-based to 1-based
              .perPage(pageable.getPageSize())
              .build();
    }
  }
}
