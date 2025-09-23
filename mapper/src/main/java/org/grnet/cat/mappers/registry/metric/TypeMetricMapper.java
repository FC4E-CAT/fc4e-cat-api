package org.grnet.cat.mappers.registry.metric;

import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.registry.metric.TypeMetricResponseDto;
import org.grnet.cat.dtos.registry.metric.TypeMetricUpdateDto;
import org.grnet.cat.entities.registry.metric.TypeMetric;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Mapper(imports = {StringUtils.class, Timestamp.class, java.time.Instant.class, Objects.class},
        uses = {TypeReproducibilityMapper.class})

public interface TypeMetricMapper {

    TypeMetricMapper INSTANCE = Mappers.getMapper(TypeMetricMapper.class);

    @Named("map")
    @Mapping(target = "typeReproducibilityId", expression = "java(typeMetric.getTypeReproducibility().getId())")
    TypeMetricResponseDto typeMetricToDto(TypeMetric typeMetric);

    @IterableMapping(qualifiedByName = "map")
    List<TypeMetricResponseDto> typeMetricToDtos(List<TypeMetric> entities);


    @Mapping(target = "labelTypeMetric", expression = "java(StringUtils.isNotEmpty(request.labelTypeMetric) ? request.labelTypeMetric : typeMetric.getLabelTypeMetric())")
    @Mapping(target = "descTypeMetric", expression = "java(StringUtils.isNotEmpty(request.descTypeMetric) ? request.descTypeMetric : typeMetric.getDescTypeMetric())")
    @Mapping(target = "descMetricApproach", expression = "java(StringUtils.isNotEmpty(request.descMetricApproach) ? request.descMetricApproach : typeMetric.getDescMetricApproach())")
    @Mapping(target = "descBenchmarkApproach", expression = "java(StringUtils.isNotEmpty(request.descBenchmarkApproach) ? request.descBenchmarkApproach : typeMetric.getDescBenchmarkApproach())")
    @Mapping(target = "uriTypeMetric", expression = "java(StringUtils.isNotEmpty(request.uriTypeMetric) ? request.uriTypeMetric : typeMetric.getUriTypeMetric())")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "TMT", ignore = true)
    @Mapping(target = "lodTMT_V", ignore = true)
    @Mapping(target = "populatedBy", ignore = true)
    @Mapping(target = "lastTouch", expression = "java(Timestamp.from(Instant.now()))")
    @Mapping(target = "enabled", expression = "java(Objects.nonNull(request.enabled) ? request.enabled : typeMetric.getEnabled())")
    void updateTypeMetricFromDto(TypeMetricUpdateDto request, @MappingTarget TypeMetric typeMetric);

}
