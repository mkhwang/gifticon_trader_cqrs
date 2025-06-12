package com.mkhwang.trader.common.dto;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;

class PaginationDtoTest {

  @Test
  @DisplayName("기본 정렬 파라미터가 주어지면 DESC 정렬로 변환된다")
  void shouldCreateDescSortByDefault() {
    PaginationDto.PaginationRequest request = PaginationDto.PaginationRequest.builder()
            .page(1)
            .size(10)
            .sort("createdAt:desc")
            .build();

    Pageable pageable = request.toPageable();

    assertEquals(0, pageable.getPageNumber()); // 1-based to 0-based
    assertEquals(10, pageable.getPageSize());

    Sort.Order order = pageable.getSort().getOrderFor("createdAt");
    assertNotNull(order);
    assertEquals(Sort.Direction.DESC, order.getDirection());
  }

  @Test
  @DisplayName("asc 정렬 파라미터가 주어지면 ASC 정렬로 변환된다")
  void shouldCreateAscSort() {
    PaginationDto.PaginationRequest request = PaginationDto.PaginationRequest.builder()
            .page(2)
            .size(20)
            .sort("name:asc")
            .build();

    Pageable pageable = request.toPageable();

    assertEquals(1, pageable.getPageNumber()); // page=2 → 1 (0-based)
    assertEquals(20, pageable.getPageSize());

    Sort.Order order = pageable.getSort().getOrderFor("name");
    assertNotNull(order);
    assertEquals(Sort.Direction.ASC, order.getDirection());
  }


  @Test
  @DisplayName("PaginationInfo.empty는 총 아이템 0, 총 페이지 0을 반환한다")
  void shouldReturnEmptyPaginationInfo() {
    Pageable pageable = PaginationDto.PaginationRequest.builder()
            .page(3)
            .size(15)
            .sort("createdAt:asc")
            .build().toPageable();

    PaginationDto.PaginationInfo emptyInfo = PaginationDto.PaginationInfo.empty(pageable);

    assertEquals(0, emptyInfo.getTotalItems());
    assertEquals(0, emptyInfo.getTotalPages());
    assertEquals(3, emptyInfo.getCurrentPage()); // 1-based
    assertEquals(15, emptyInfo.getPerPage());
  }
}