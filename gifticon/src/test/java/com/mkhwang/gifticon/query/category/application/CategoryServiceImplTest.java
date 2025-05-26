package com.mkhwang.gifticon.query.category.application;

import com.mkhwang.gifticon.common.config.GenericMapper;
import com.mkhwang.gifticon.common.config.QuerydslUtil;
import com.mkhwang.gifticon.common.dto.PaginationDto;
import com.mkhwang.gifticon.query.category.domain.Category;
import com.mkhwang.gifticon.query.category.infra.CategoryRepository;
import com.mkhwang.gifticon.query.category.presentation.dto.CategoryDto;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
  @Mock
  CategoryRepository categoryRepository;
  @Mock
  GenericMapper genericMapper;
  @Mock
  QuerydslUtil querydslUtil;
  @Mock
  JPAQueryFactory jpaQueryFactory;

  @InjectMocks
  CategoryServiceImpl categoryService;

  @DisplayName("getAllCategories: 모든 카테고리를 조회한다.")
  @Test
  void getAllCategories() {
    // given
    Category parent = new Category();
    parent.setId(1L);
    parent.setLevel(1);
    Category child = new Category();
    child.setId(2L);
    child.setLevel(2);
    child.setParent(parent);

    CategoryDto.Category parentDto = new CategoryDto.Category();
    parentDto.setId(1L);
    parentDto.setLevel(1);
    parentDto.setChildren(new ArrayList<>());

    CategoryDto.Category childDto = new CategoryDto.Category();
    childDto.setId(2L);
    childDto.setLevel(2);

    when(categoryRepository.findAll()).thenReturn(List.of(parent, child));
    when(genericMapper.toDto(parent, CategoryDto.Category.class)).thenReturn(parentDto);
    when(genericMapper.toDto(child, CategoryDto.Category.class)).thenReturn(childDto);

    // when
    List<CategoryDto.Category> result = categoryService.getAllCategories(null);

    // then
    assertEquals(1, result.size());
    assertEquals(1, result.get(0).getChildren().size());
    assertEquals(2L, result.get(0).getChildren().get(0).getId());
  }

  @DisplayName("카테고리 기프티콘을 조회 한다.")
  @Test
  void getCategoryGifticons() {
    // given
    Long categoryId = 1L;
    Boolean includeSubcategories = true;
    PaginationDto.PaginationRequest paginationRequest = new PaginationDto.PaginationRequest(0, 10, null);

    // when

    // then

  }
}