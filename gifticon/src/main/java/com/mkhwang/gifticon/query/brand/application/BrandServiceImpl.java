package com.mkhwang.gifticon.query.brand.application;

import com.mkhwang.gifticon.common.config.GenericMapper;
import com.mkhwang.gifticon.query.brand.infra.BrandRepository;
import com.mkhwang.gifticon.query.brand.presentation.dto.BrandDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService{
  private final GenericMapper genericMapper;
  private final BrandRepository brandRepository;

  @Override
  @Transactional(readOnly = true)
  public List<BrandDto.Brand> getAllBrands() {
    return genericMapper.toDtoList(brandRepository.findAll(), BrandDto.Brand.class);
  }
}
