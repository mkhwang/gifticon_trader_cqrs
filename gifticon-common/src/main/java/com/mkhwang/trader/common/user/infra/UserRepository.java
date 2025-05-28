package com.mkhwang.trader.common.user.infra;

import com.mkhwang.trader.common.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Page<User> findByNicknameContainingIgnoreCase(String nickname, Pageable pageable);
}
