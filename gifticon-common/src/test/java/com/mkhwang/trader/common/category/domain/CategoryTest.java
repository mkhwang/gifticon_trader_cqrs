package com.mkhwang.trader.common.category.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

  @DisplayName("addChild() 호출 시 자식의 부모와 레벨이 설정되고, 부모의 자식 목록에 포함된다")
  @Test
  void addChild_shouldSetParentAndLevelAndAddToChildrenList() {
    // given
    Category parent = Category.builder()
            .id(1L)
            .name("parent")
            .slug("parent")
            .description("parent category")
            .level(1)
            .build();

    Category child = Category.builder()
            .id(2L)
            .name("Child")
            .slug("child")
            .description("Subcategory")
            .build();

    // when
    parent.addChild(child);

    // then
    assertEquals(parent, child.getParent());
    assertEquals(2, child.getLevel());
    assertTrue(parent.getChildren().contains(child));
  }
}