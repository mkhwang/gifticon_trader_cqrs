package com.mkhwang.trader.common.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ModelMapperConfigTest {

  @Test
  void shouldReturnStrictConfiguredModelMapper() {
    // given
    ModelMapperConfig config = new ModelMapperConfig();

    // when
    ModelMapper mapper = config.modelMapper();

    // then
    assertEquals(MatchingStrategies.STRICT, mapper.getConfiguration().getMatchingStrategy());
    assertTrue(mapper.getConfiguration().isFieldMatchingEnabled());
    assertEquals(Configuration.AccessLevel.PRIVATE, mapper.getConfiguration().getFieldAccessLevel());
  }
}