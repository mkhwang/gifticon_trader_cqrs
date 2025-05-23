package com.mkhwang.gifticon.controller;

import com.mkhwang.gifticon.service.UserService;
import com.mkhwang.gifticon.service.dto.PaginationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "User API")
@RestController
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @Operation(summary = "사용자 조회", description = "사용자 목록 조회 API")
  @GetMapping("/api/users")
  public ResponseEntity<?> getAllUsers(
          @RequestParam(required = false) String keyword,
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "10") int perPage,
          @RequestParam(defaultValue = "id:asc") String sort
  ) {
    PaginationDto.PaginationRequest paginationRequest = PaginationDto.PaginationRequest.builder()
            .page(page)
            .size(perPage)
            .sort(sort)
            .build();

    return ResponseEntity.ok(ApiResponse.success(userService.getAllUsers(keyword, paginationRequest),
            "사용자 목록을 성공적으로 조회했습니다."));
  }

  @Operation(summary = "사용자 리뷰 조회", description = "사용자 리뷰 목록 조회 API")
  @GetMapping("/api/users/{id}/reviews")
  public ResponseEntity<?> getUserReviews(
          @PathVariable Long id,
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "10") int perPage,
          @RequestParam(defaultValue = "id:asc") String sort) {
    PaginationDto.PaginationRequest paginationRequest = PaginationDto.PaginationRequest.builder()
            .page(page)
            .size(perPage)
            .sort(sort)
            .build();

    return ResponseEntity.ok(ApiResponse.success(userService.getUserReviews(id, paginationRequest),
            "리뷰 목록을 성공적으로 조회했습니다."));
  }
}
