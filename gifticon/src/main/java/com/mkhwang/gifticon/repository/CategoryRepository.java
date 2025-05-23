package com.mkhwang.gifticon.repository;

import com.mkhwang.gifticon.service.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  List<Category> findByLevel(Integer level);
}
