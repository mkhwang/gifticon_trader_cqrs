package com.mkhwang.gifticon.service;

import com.mkhwang.gifticon.service.dto.BrandDto;

import java.util.List;

public interface BrandService {
  List<BrandDto.Brand> getAllBrands();
}
