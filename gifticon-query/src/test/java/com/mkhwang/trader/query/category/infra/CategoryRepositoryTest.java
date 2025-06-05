package com.mkhwang.trader.query.category.infra;

import com.mkhwang.trader.common.category.domain.Category;
import com.mkhwang.trader.common.category.infra.CategoryRepository;
import com.mkhwang.trader.query.config.TestJpaConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Transactional
@DataJpaTest
@Import(TestJpaConfig.class)
class CategoryRepositoryTest {
  @Autowired
  private CategoryRepository categoryRepository;

  @DisplayName("findByLevel 정상동작 테스트")
  @Test
  void findByLevel() {
    // given
    categoryRepository.save(Category.builder().name("1차 카테고리").slug("1").level(1).build());
    categoryRepository.save(Category.builder().name("2차 카테고리").slug("2").level(2).build());

    // when
    List<Category> result = categoryRepository.findByLevel(1);

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getLevel()).isEqualTo(1);
  }
}