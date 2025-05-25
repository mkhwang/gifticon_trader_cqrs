package com.mkhwang.gifticon.query.user.infra;

import com.mkhwang.gifticon.query.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Page<User> findByNicknameContainingIgnoreCase(String nickname, Pageable pageable);
}
