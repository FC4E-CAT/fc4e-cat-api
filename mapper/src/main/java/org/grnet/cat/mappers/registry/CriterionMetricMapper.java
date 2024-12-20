package org.grnet.cat.mappers.registry;

import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.registry.CriterionMetricResponseDto;
import org.grnet.cat.entities.registry.CriterionMetricJunction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Mapper(imports = {StringUtils.class, Timestamp.class, Instant.class})
public interface CriterionMetricMapper {


    CriterionMetricMapper INSTANCE = Mappers.getMapper(CriterionMetricMapper.class);

    List<CriterionMetricResponseDto> criterionMetricToResponseDtos(List<CriterionMetricJunction> entities);

    @Mapping(target = "metricId", expression = "java(criterionMetric.getMetric().getId())")
    @Mapping(target = "criterionId", expression = "java(criterionMetric.getCriterion().getId())")
    @Mapping(target = "motivationId", expression = "java(criterionMetric.getMotivation().getId())")
    @Mapping(target = "relationId", expression = "java(criterionMetric.getRelation().getId())")
    CriterionMetricResponseDto criterionMetricToDto(CriterionMetricJunction criterionMetric);

}
