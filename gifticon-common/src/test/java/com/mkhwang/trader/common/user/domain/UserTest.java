package com.mkhwang.trader.common.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserTest {


  @DisplayName("user of 생성자 테스트")
  @Test
  void of() {
    // given
    String userName = "username";
    String nickName = "nickname";
    String profileUrl = "https://image.profile.url";

    // when
    User user = User.of(userName, nickName, profileUrl);

    // then
    assertNotNull(user);
    assertEquals(userName, user.getUsername());
    assertEquals(nickName, user.getNickname());
    assertEquals(profileUrl, user.getProfileImageUrl());
  }
}