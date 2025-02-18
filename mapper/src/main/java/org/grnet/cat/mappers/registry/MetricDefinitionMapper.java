package org.grnet.cat.mappers.registry;

import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.registry.MetricDefinitionExtendedResponse;
import org.grnet.cat.dtos.registry.MetricDefinitionResponseDto;
import org.grnet.cat.entities.registry.MetricDefinitionJunction;
import org.grnet.cat.mappers.registry.metric.MetricMapper;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(imports = {StringUtils.class, java.sql.Timestamp.class, java.time.Instant.class}, uses = {MetricMapper.class, TypeBenchmarkMapper.class})
public interface MetricDefinitionMapper {

    MetricDefinitionMapper INSTANCE = Mappers.getMapper(MetricDefinitionMapper.class);

    @IterableMapping(qualifiedByName = "map")
    List<MetricDefinitionResponseDto> metricDefinitionToResponseDtos(List<MetricDefinitionJunction> metricDefinitionJunctions);

    @Named("map")
    @Mapping(target = "metricId", expression = "java(metricDefinitionJunction.getMetric().getId())")
    @Mapping(target = "typeBenchmarkId",  expression = "java(metricDefinitionJunction.getTypeBenchmark().getId())")
    @Mapping(target = "motivationId", expression = "java(metricDefinitionJunction.getMotivation().getId())")
    MetricDefinitionResponseDto metricDefinitionToResponseDto(MetricDefinitionJunction metricDefinitionJunction);

    @IterableMapping(qualifiedByName = "mapToExtendedResponse")
    List<MetricDefinitionExtendedResponse> metricDefinitionToExtendedResponses(List<MetricDefinitionJunction> metricDefinitionJunctions);

    @Named("mapToExtendedResponse")
    @Mapping(target = "metricId", expression = "java(metricDefinitionJunction.getMetric().getId())")
    @Mapping(target = "metricMtr", expression = "java(metricDefinitionJunction.getMetric().getMTR())")
    @Mapping(target = "metricLabel", expression = "java(metricDefinitionJunction.getMetric().getLabelMetric())")
    @Mapping(target = "metricDescription", expression = "java(metricDefinitionJunction.getMetric().getDescrMetric())")
    @Mapping(target = "typeAlgorithmId", expression = "java(metricDefinitionJunction.getMetric().getTypeAlgorithm().getId())")
    @Mapping(target = "typeAlgorithmLabel", expression = "java(metricDefinitionJunction.getMetric().getTypeAlgorithm().getLabelAlgorithmType())")
    @Mapping(target = "typeAlgorithmDescription", expression = "java(metricDefinitionJunction.getMetric().getTypeAlgorithm().getDescAlgorithmType())")
    @Mapping(target = "typeMetricId", expression = "java(metricDefinitionJunction.getMetric().getTypeMetric().getId())")
    @Mapping(target = "typeMetricLabel", expression = "java(metricDefinitionJunction.getMetric().getTypeMetric().getLabelTypeMetric())")
    @Mapping(target = "typeMetricDescription", expression = "java(metricDefinitionJunction.getMetric().getTypeMetric().getDescTypeMetric())")
    @Mapping(target = "typeBenchmarkId", expression = "java(metricDefinitionJunction.getTypeBenchmark().getId())")
    @Mapping(target = "typeBenchmarkLabel", expression = "java(metricDefinitionJunction.getTypeBenchmark().getLabelBenchmarkType())")
    @Mapping(target = "typeBenchmarkDescription", expression = "java(metricDefinitionJunction.getTypeBenchmark().getDescBenchmarkType())")
    @Mapping(target = "typeBenchmarkPatter", expression = "java(metricDefinitionJunction.getTypeBenchmark().getPattern())")
    @Mapping(target = "motivationId", expression = "java(metricDefinitionJunction.getMotivation().getId())")
    @Mapping(target = "valueBenchmark", expression = "java(metricDefinitionJunction.getValueBenchmark())")
    MetricDefinitionExtendedResponse metricDefinitionToExtendedResponse(MetricDefinitionJunction metricDefinitionJunction);
}
