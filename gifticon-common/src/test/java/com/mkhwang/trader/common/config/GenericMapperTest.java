package com.mkhwang.trader.common.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class GenericMapperTest {

  @Mock
  private ModelMapper modelMapper;

  private GenericMapper genericMapper;

  @BeforeEach
  void setUp() {
    genericMapper = new GenericMapper(modelMapper);
  }

  static class DummyDto {
    String name;

    // getter, setter, constructor
    DummyDto(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

  static class DummyEntity {
    String name;

    // getter, setter, constructor
    DummyEntity(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

  @Test
  void testToEntity() {
    // given
    DummyDto dto = new DummyDto("test");
    DummyEntity entity = new DummyEntity("test");

    given(modelMapper.map(dto, DummyEntity.class)).willReturn(entity);

    // when
    DummyEntity result = genericMapper.toEntity(dto, DummyEntity.class);

    // then
    assertEquals(entity, result);
    verify(modelMapper).map(dto, DummyEntity.class);
  }

  @Test
  void testUpdateEntity() {
    // given
    DummyDto dto = new DummyDto("updated");
    DummyEntity entity = new DummyEntity("original");

    // when
    DummyEntity result = genericMapper.updateEntity(dto, entity);

    // then
    assertEquals(entity, result);
    verify(modelMapper).map(dto, entity);
  }

  @Test
  void testToDto() {
    // given
    DummyEntity entity = new DummyEntity("test");
    DummyDto dto = new DummyDto("test");

    given(modelMapper.map(entity, DummyDto.class)).willReturn(dto);

    // when
    DummyDto result = genericMapper.toDto(entity, DummyDto.class);


    // then
    assertEquals(dto, result);
    verify(modelMapper).map(entity, DummyDto.class);
  }

  @Test
  void testToEntityList() {
    DummyDto dto1 = new DummyDto("a");
    DummyDto dto2 = new DummyDto("b");
    List<DummyDto> dtoList = List.of(dto1, dto2);

    DummyEntity entity1 = new DummyEntity("a");
    DummyEntity entity2 = new DummyEntity("b");

    given(modelMapper.map(dto1, DummyEntity.class)).willReturn(entity1);
    given(modelMapper.map(dto2, DummyEntity.class)).willReturn(entity2);

    // when
    List<DummyEntity> result = genericMapper.toEntityList(dtoList, DummyEntity.class);

    // then
    assertThat(result).containsExactly(entity1, entity2);
  }

  @Test
  void testToDtoList() {
    // given
    DummyEntity e1 = new DummyEntity("x");
    DummyEntity e2 = new DummyEntity("y");
    List<DummyEntity> entityList = List.of(e1, e2);

    DummyDto d1 = new DummyDto("x");
    DummyDto d2 = new DummyDto("y");

    given(modelMapper.map(e1, DummyDto.class)).willReturn(d1);
    given(modelMapper.map(e2, DummyDto.class)).willReturn(d2);

    // when
    List<DummyDto> result = genericMapper.toDtoList(entityList, DummyDto.class);

    // then
    assertThat(result).containsExactly(d1, d2);
  }
}