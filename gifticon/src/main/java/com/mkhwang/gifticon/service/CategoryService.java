package com.mkhwang.gifticon.service;

import com.mkhwang.gifticon.service.dto.CategoryDto;
import com.mkhwang.gifticon.service.dto.PaginationDto;

import java.util.List;

public interface CategoryService {

  List<CategoryDto.Category> getAllCategories(Integer level);

  CategoryDto.CategoryGifticon getCategoryGifticons(
          Long categoryId,
          Boolean includeSubcategories,
          PaginationDto.PaginationRequest paginationRequest
  );
}
