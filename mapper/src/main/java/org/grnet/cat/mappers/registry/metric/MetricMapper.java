package org.grnet.cat.mappers.registry.metric;

import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.registry.metric.MetricRequestDto;
import org.grnet.cat.dtos.registry.metric.MetricResponseDto;
import org.grnet.cat.dtos.registry.metric.MetricUpdateDto;
import org.grnet.cat.entities.registry.metric.Metric;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Mapper(imports = {StringUtils.class, Timestamp.class, Instant.class}, uses = {TypeMetricMapper.class, TypeAlgorithmMapper.class})
public interface MetricMapper {

    MetricMapper INSTANCE = Mappers.getMapper(MetricMapper.class);

    List<MetricResponseDto> metricToDtos(List<Metric> entities);

    @Named("map")
    @Mapping(target = "typeAlgorithmId", expression = "java(metric.getTypeAlgorithm().getId())")
    @Mapping(target = "typeMetricId", expression = "java(metric.getTypeMetric().getId())")
    MetricResponseDto metricToDto(Metric metric);

    @Named("mapWithExpression")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "populatedBy", ignore = true)
    @Mapping(target = "lastTouch", expression = "java(Timestamp.from(Instant.now()))")
    @Mapping(target = "typeAlgorithm", ignore = true)
    @Mapping(target = "typeMetric", ignore = true)
    Metric metricToEntity(MetricRequestDto request);

    @Mapping(target = "MTR", expression = "java(StringUtils.isNotEmpty(request.MTR) ? request.MTR : metric.getMTR())")
    @Mapping(target = "labelMetric", expression = "java(StringUtils.isNotEmpty(request.labelMetric) ? request.labelMetric : metric.getLabelMetric())")
    @Mapping(target = "descrMetric", expression = "java(StringUtils.isNotEmpty(request.descrMetric) ? request.descrMetric : metric.getDescrMetric())")
    @Mapping(target = "urlMetric", expression = "java(StringUtils.isNotEmpty(request.urlMetric) ? request.urlMetric : metric.getUrlMetric())")
    @Mapping(target = "lodMTV", expression = "java(StringUtils.isNotEmpty(request.lodMTV) ? request.lodMTV : metric.getLodMTV())")
    @Mapping(target = "lastTouch", expression = "java(Timestamp.from(Instant.now()))")
    @Mapping(target = "typeAlgorithm", ignore = true)
    @Mapping(target = "typeMetric", ignore = true)
    @Mapping(target = "populatedBy", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateMetricFromDto(MetricUpdateDto request, @MappingTarget Metric metric);

}
