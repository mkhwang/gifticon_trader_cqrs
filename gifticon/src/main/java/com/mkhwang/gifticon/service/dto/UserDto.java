package com.mkhwang.gifticon.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class UserDto {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class User {
    private Long id;
    private String username;
    private String nickname;
    private String profileImageUrl;
  }

  @Data
  @Builder
  public static class UserPage {
    private List<User> users;
    private PaginationDto.PaginationInfo pagination;
  }
}
