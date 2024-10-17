package org.grnet.cat.mappers.registry;

import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.registry.MetricDefinitionResponseDto;
import org.grnet.cat.entities.registry.MetricDefinitionJunction;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(imports = {StringUtils.class, java.sql.Timestamp.class, java.time.Instant.class})
public interface MetricDefinitionMapper {

    MetricDefinitionMapper INSTANCE = Mappers.getMapper(MetricDefinitionMapper.class);

    @IterableMapping(qualifiedByName = "map")
    List<MetricDefinitionResponseDto> metricDefinitionToResponseDtos(List<MetricDefinitionJunction> metricDefinitionJunctions);

    @Named("map")
    @Mapping(target = "metricId", expression = "java(metricDefinitionJunction.getMetric().getId())")
    @Mapping(target = "typeBenchmarkId",  expression = "java(metricDefinitionJunction.getTypeBenchmark().getId())")
    @Mapping(target = "motivationId", expression = "java(metricDefinitionJunction.getMotivation().getId())")
    MetricDefinitionResponseDto metricDefinitionToResponseDto(MetricDefinitionJunction metricDefinitionJunction);

}
