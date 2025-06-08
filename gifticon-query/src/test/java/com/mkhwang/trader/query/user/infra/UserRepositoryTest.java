package com.mkhwang.trader.query.user.infra;

import com.mkhwang.trader.common.user.domain.User;
import com.mkhwang.trader.common.user.infra.UserRepository;
import support.TestJpaConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@DataJpaTest
@Import(TestJpaConfig.class)
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @DisplayName("닉네임 필터 테스트")
  @Test
  void findByNicknameContainingIgnoreCase() {
    // given
    User user1 = User.of("user1", "user1", "http://image.user.com");
    User user2 = User.of("user2", "user2", "http://image.user.com");
    User user3 = User.of("people3", "people3", "http://image.user.com");
    userRepository.saveAll(List.of(user1, user2, user3));

    // when
    Page<User> result = userRepository.findByNicknameContainingIgnoreCase("user", PageRequest.of(0, 10));

    // then
    assertNotNull(result);
    assertEquals(2, result.getTotalElements());
    assertEquals(1, result.getTotalPages());
  }
}