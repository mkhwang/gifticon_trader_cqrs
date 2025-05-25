package com.mkhwang.gifticon.query.brand.application;

import com.mkhwang.gifticon.query.brand.presentation.dto.BrandDto;

import java.util.List;

public interface BrandService {
  List<BrandDto.Brand> getAllBrands();
}
