package com.mkhwang.gifticon.command.tag.infra;

import com.mkhwang.gifticon.command.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
