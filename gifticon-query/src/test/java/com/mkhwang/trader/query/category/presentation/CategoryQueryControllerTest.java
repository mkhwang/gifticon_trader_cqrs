package com.mkhwang.trader.query.category.presentation;

import com.mkhwang.trader.common.dto.PaginationDto;
import com.mkhwang.trader.query.category.application.CategoryService;
import com.mkhwang.trader.query.category.presentation.dto.CategoryDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryQueryControllerTest {
  @Mock
  CategoryService categoryService;

  @InjectMocks
  CategoryQueryController categoryQueryController;

  @DisplayName("getAllCategories: 모든 카테고리를 조회한다.")
  @Test
  void getAllCategories() {
    // given
    Integer level = 1;
    List mockList = mock(List.class);
    given(categoryService.getAllCategories(level)).willReturn(mockList);

    // when
    ResponseEntity<?> response = categoryQueryController.getAllCategories(level);

    // then
    verify(categoryService).getAllCategories(level);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

  }

  @DisplayName("getCategoryGifticons: 카테고리 기프티콘을 조회한다.")
  @Test
  void getCategoryGifticons() {
    // given
    Long categoryId = 1L;
    int page = 1;
    int perPage = 10;
    String sort = "createdAt:desc";
    Boolean includeSubcategories = true;

    CategoryDto.CategoryGifticon categoryGifticon = mock(CategoryDto.CategoryGifticon.class);

    given(categoryService.getCategoryGifticons(
            eq(categoryId), eq(includeSubcategories), any(PaginationDto.PaginationRequest.class)))
            .willReturn(categoryGifticon);

    // when
    ResponseEntity<?> response = categoryQueryController.getCategoryGifticons(
            categoryId, page, perPage, sort, includeSubcategories);

    // then
    verify(categoryService).getCategoryGifticons(eq(categoryId), eq(includeSubcategories),
            any(PaginationDto.PaginationRequest.class));
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }
}