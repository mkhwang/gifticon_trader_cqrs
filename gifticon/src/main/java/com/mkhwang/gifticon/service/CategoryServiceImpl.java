package com.mkhwang.gifticon.service;

import com.mkhwang.gifticon.config.GenericMapper;
import com.mkhwang.gifticon.repository.CategoryRepository;
import com.mkhwang.gifticon.service.dto.CategoryDto;
import com.mkhwang.gifticon.service.dto.PaginationDto;
import com.mkhwang.gifticon.service.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
  private final GenericMapper genericMapper;
  private final CategoryRepository categoryRepository;

  @Override
  @Transactional(readOnly = true)
  public List<CategoryDto.Category> getAllCategories(Integer level) {
    List<Category> all = categoryRepository.findAll();
    List<CategoryDto.Category> root = new ArrayList<>();

    for (Category category : all) {
      if (category.getLevel() == null || category.getLevel() == 1) {
        root.add(genericMapper.toDto(category, CategoryDto.Category.class));
      } else {
        root.stream()
                .filter(c -> c.getId().equals(category.getParent().getId()))
                .findFirst().ifPresent(parent ->
                        parent.getChildren().add(genericMapper.toDto(category, CategoryDto.Category.class)));
      }
    }

    if (level != null) {
      return root.stream().filter(
                      c -> c.getLevel() != null && c.getLevel().equals(level))
              .toList();
    }
    return root;
  }

  @Override
  @Transactional(readOnly = true)
  public CategoryDto.CategoryGifticon getCategoryGifticons(Long categoryId, Boolean includeSubcategories,
                                                           PaginationDto.PaginationRequest paginationRequest) {
    return null;
  }
}
