package com.mkhwang.gifticon.query.category.infra;

import com.mkhwang.gifticon.query.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  List<Category> findByLevel(Integer level);
}
