package com.mkhwang.gifticon.repository;

import com.mkhwang.gifticon.service.entity.Review;
import com.mkhwang.gifticon.service.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
  Page<Review> findByUser(User user, Pageable pageable);
}
