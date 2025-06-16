package com.mkhwang.trader.query.category.application;

import com.mkhwang.trader.common.category.domain.Category;
import com.mkhwang.trader.common.category.infra.CategoryRepository;
import com.mkhwang.trader.common.config.GenericMapper;
import com.mkhwang.trader.query.category.presentation.dto.CategoryDto;
import com.mkhwang.trader.query.config.QuerydslUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

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

    given(categoryRepository.findAll()).willReturn(List.of(parent, child));
    given(genericMapper.toDto(parent, CategoryDto.Category.class)).willReturn(parentDto);
    given(genericMapper.toDto(child, CategoryDto.Category.class)).willReturn(childDto);

    // when
    List<CategoryDto.Category> result = categoryService.getAllCategories(null);

    // then
    assertEquals(1, result.size());
    assertEquals(1, result.get(0).getChildren().size());
    assertEquals(2L, result.get(0).getChildren().get(0).getId());
  }
}