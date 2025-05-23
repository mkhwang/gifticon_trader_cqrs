package com.mkhwang.gifticon.controller;

import com.mkhwang.gifticon.service.CategoryService;
import com.mkhwang.gifticon.service.dto.CategoryDto;
import com.mkhwang.gifticon.service.dto.PaginationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Category", description = "Category API")
@RestController
@RequiredArgsConstructor
public class CategoryController {
  private final CategoryService categoryService;

  @Operation(summary = "Category 조회", description = "Category 목록 조회 API")
  @GetMapping("/api/categories")
  public ResponseEntity<?> getAllCategories(@RequestParam(required = false) Integer level) {
    return ResponseEntity.ok(
            ApiResponse.success(
                    categoryService.getAllCategories(level),
                    "카테고리 목록을 성공적으로 조회했습니다."
            )
    );
  }

  @Operation(summary = "Category 기프티콘 조회", description = "Category 기프티콘 조회 API")
  @GetMapping("/api/categories/{id}/gifticons")
  public ResponseEntity<?> getCategoryGifticons(
          @PathVariable Long id,
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "10") int perPage,
          @RequestParam(defaultValue = "created_at:desc") String sort,
          @RequestParam(defaultValue = "true") Boolean includeSubcategories) {

    PaginationDto.PaginationRequest paginationRequest = PaginationDto.PaginationRequest.builder()
            .page(page)
            .size(perPage)
            .sort(sort)
            .build();

    CategoryDto.CategoryGifticon response =
            categoryService.getCategoryGifticons(id, includeSubcategories, paginationRequest);

    return ResponseEntity.ok(
            ApiResponse.success(
                    response,
                    "카테고리 상품 목록을 성공적으로 조회했습니다."
            )
    );
  }
}
