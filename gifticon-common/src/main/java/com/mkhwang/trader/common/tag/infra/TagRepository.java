package com.mkhwang.trader.common.tag.infra;

import com.mkhwang.trader.common.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
