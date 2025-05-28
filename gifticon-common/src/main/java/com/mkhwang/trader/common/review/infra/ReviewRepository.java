package com.mkhwang.trader.common.review.infra;

import com.mkhwang.trader.common.review.domain.Review;
import com.mkhwang.trader.common.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
  Page<Review> findByUser(User user, Pageable pageable);
}
