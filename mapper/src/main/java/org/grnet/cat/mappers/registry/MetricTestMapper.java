package org.grnet.cat.mappers.registry;

import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.registry.MetricTestRequestDto;
import org.grnet.cat.dtos.registry.MetricTestResponseDto;
import org.grnet.cat.entities.registry.MetricTestJunction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Mapper(imports = {StringUtils.class, Timestamp.class, Instant.class})
public interface MetricTestMapper {

    MetricTestMapper INSTANCE = Mappers.getMapper(MetricTestMapper.class);

    List<MetricTestResponseDto> metricTestToDtos(List<MetricTestJunction> entities);

    @Mapping(target = "testId", expression = "java(metricTest.getTest().getId())")
    @Mapping(target = "motivationId", expression = "java(metricTest.getMotivation().getId())")
    @Mapping(target = "relation", expression = "java(metricTest.getRelation().getId())")
    MetricTestResponseDto metricTestToDto(MetricTestJunction metricTest);

}
