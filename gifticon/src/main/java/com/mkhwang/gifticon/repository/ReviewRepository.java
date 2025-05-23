package com.mkhwang.gifticon.repository;

import com.mkhwang.gifticon.service.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
