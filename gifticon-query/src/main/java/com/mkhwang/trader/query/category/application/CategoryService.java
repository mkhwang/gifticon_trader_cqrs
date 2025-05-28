package com.mkhwang.trader.query.category.application;

import com.mkhwang.trader.common.dto.PaginationDto;
import com.mkhwang.trader.query.category.presentation.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

  List<CategoryDto.Category> getAllCategories(Integer level);

  CategoryDto.CategoryGifticon getCategoryGifticons(
          Long categoryId,
          Boolean includeSubcategories,
          PaginationDto.PaginationRequest paginationRequest
  );
}
