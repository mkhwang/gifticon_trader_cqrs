package com.mkhwang.gifticon.repository;

import com.mkhwang.gifticon.service.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
