package org.grnet.cat.mappers.registry.metric;

import org.grnet.cat.dtos.registry.metric.TypeMetricResponseDto;
import org.grnet.cat.entities.registry.metric.TypeMetric;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(imports = {java.sql.Timestamp.class, java.time.Instant.class},
        uses = {TypeReproducibilityMapper.class})

public interface TypeMetricMapper {

    TypeMetricMapper INSTANCE = Mappers.getMapper(TypeMetricMapper.class);

    @Named("map")
    @Mapping(target = "typeReproducibilityId", expression = "java(typeMetric.getTypeReproducibility().getId())")
    @Mapping(target = "metrics", ignore = true)
    TypeMetricResponseDto typeMetricToDto(TypeMetric typeMetric);

    @IterableMapping(qualifiedByName = "map")
    List<TypeMetricResponseDto> typeMetricToDtos(List<TypeMetric> entities);
}
