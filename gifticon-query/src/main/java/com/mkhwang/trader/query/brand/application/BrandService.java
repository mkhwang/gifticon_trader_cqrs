package com.mkhwang.trader.query.brand.application;

import com.mkhwang.trader.query.brand.presentation.dto.BrandDto;

import java.util.List;

public interface BrandService {
  List<BrandDto.Brand> getAllBrands();
}
