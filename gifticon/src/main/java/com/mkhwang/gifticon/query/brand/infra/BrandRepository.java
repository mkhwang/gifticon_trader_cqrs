package com.mkhwang.gifticon.query.brand.infra;

import com.mkhwang.gifticon.query.brand.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}
