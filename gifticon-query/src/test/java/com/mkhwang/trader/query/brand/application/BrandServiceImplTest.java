package com.mkhwang.trader.query.brand.application;

import com.mkhwang.trader.common.brand.infra.BrandRepository;
import com.mkhwang.trader.common.config.GenericMapper;
import com.mkhwang.trader.query.brand.presentation.dto.BrandDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BrandServiceImplTest {
  @Mock
  GenericMapper genericMapper;
  @Mock
  BrandRepository brandRepository;
  @InjectMocks
  BrandServiceImpl brandService;

  @DisplayName("모든 브랜드를 조회한다.")
  @Test
  void getAllBrands() {
    // Given
    List mock = mock(List.class);
    given(brandRepository.findAll()).willReturn(mock);

    // When
    brandService.getAllBrands();

    // Then
    verify(brandRepository).findAll();
    verify(genericMapper).toDtoList(mock, BrandDto.Brand.class);
  }
}