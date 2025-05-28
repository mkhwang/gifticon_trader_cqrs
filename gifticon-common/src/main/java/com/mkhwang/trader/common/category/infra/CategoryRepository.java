package com.mkhwang.trader.common.category.infra;

import com.mkhwang.trader.common.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  List<Category> findByLevel(Integer level);
}
