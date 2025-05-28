package com.mkhwang.trader.common.gifticon.infra;

import com.mkhwang.trader.common.gifticon.domain.Gifticon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GifticonRepository extends JpaRepository<Gifticon, Long> {
}
