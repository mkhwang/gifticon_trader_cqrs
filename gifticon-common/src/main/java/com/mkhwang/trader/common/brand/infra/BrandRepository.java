package com.mkhwang.trader.common.brand.infra;

import com.mkhwang.trader.common.brand.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}
