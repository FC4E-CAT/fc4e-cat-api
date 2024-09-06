package org.grnet.cat.mappers.registry.metric;

import org.grnet.cat.dtos.registry.metric.MetricResponseDto;
import org.grnet.cat.dtos.registry.metric.TypeAlgorithmResponseDto;
import org.grnet.cat.entities.registry.metric.Metric;
import org.grnet.cat.entities.registry.metric.TypeAlgorithm;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(imports = {java.sql.Timestamp.class, java.time.Instant.class}, uses = {MetricMapper.class})
public interface TypeAlgorithmMapper {

    TypeAlgorithmMapper INSTANCE = Mappers.getMapper(TypeAlgorithmMapper.class);

    @Named("map")
    TypeAlgorithmResponseDto typeAlgorithmToDto(TypeAlgorithm entity);

    @IterableMapping(qualifiedByName = "map")
    List<TypeAlgorithmResponseDto> typeAlgorithmToDtos(List<TypeAlgorithm> entities);

    @Named("mapMetrics")
    List<MetricResponseDto> mapMetrics(List<Metric> metrics);
}
