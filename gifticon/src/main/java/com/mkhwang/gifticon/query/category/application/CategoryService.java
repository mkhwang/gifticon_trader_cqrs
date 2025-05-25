package com.mkhwang.gifticon.query.category.application;

import com.mkhwang.gifticon.common.dto.PaginationDto;
import com.mkhwang.gifticon.query.category.presentation.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

  List<CategoryDto.Category> getAllCategories(Integer level);

  CategoryDto.CategoryGifticon getCategoryGifticons(
          Long categoryId,
          Boolean includeSubcategories,
          PaginationDto.PaginationRequest paginationRequest
  );
}
