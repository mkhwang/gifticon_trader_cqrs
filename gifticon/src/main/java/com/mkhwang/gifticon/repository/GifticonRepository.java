package com.mkhwang.gifticon.repository;

import com.mkhwang.gifticon.service.entity.Gifticon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GifticonRepository extends JpaRepository<Gifticon, Long> {
}
