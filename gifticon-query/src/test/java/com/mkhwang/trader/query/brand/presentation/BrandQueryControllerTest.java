package com.mkhwang.trader.query.brand.presentation;

import com.mkhwang.trader.query.brand.application.BrandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BrandQueryControllerTest {
  @Mock
  BrandService brandService;
  @InjectMocks
  BrandQueryController brandQueryController;

  @DisplayName("getAllBrands: 모든 브랜드를 조회한다.")
  @Test
  void getAllBrands() {
    // given

    // when
    ResponseEntity<?> response = brandQueryController.getAllBrands();

    // then
    verify(brandService).getAllBrands();
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

  }
}