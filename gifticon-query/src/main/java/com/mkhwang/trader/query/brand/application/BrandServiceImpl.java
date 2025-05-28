package com.mkhwang.trader.query.brand.application;

import com.mkhwang.trader.common.brand.infra.BrandRepository;
import com.mkhwang.trader.common.config.GenericMapper;
import com.mkhwang.trader.query.brand.presentation.dto.BrandDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
  private final GenericMapper genericMapper;
  private final BrandRepository brandRepository;

  @Override
  @Transactional(readOnly = true)
  public List<BrandDto.Brand> getAllBrands() {
    return genericMapper.toDtoList(brandRepository.findAll(), BrandDto.Brand.class);
  }
}
