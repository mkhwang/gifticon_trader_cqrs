package com.mkhwang.gifticon.command.gifticon.infra;

import com.mkhwang.gifticon.command.gifticon.domain.Gifticon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GifticonRepository extends JpaRepository<Gifticon, Long> {
}
