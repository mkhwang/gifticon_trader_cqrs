package com.mkhwang.gifticon.service;

import com.mkhwang.gifticon.config.GenericMapper;
import com.mkhwang.gifticon.repository.BrandRepository;
import com.mkhwang.gifticon.service.dto.BrandDto;
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
