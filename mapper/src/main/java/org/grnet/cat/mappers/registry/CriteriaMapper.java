package org.grnet.cat.mappers.registry;

import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.registry.criterion.CriterionRequest;
import org.grnet.cat.dtos.registry.criterion.CriterionResponse;
import org.grnet.cat.dtos.registry.criterion.CriterionUpdate;
import org.grnet.cat.entities.registry.Criterion;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The CriteriaMapper is responsible for mapping Criteria entities to DTOs and vice versa.
 */
@Mapper(imports = {StringUtils.class, java.sql.Timestamp.class, java.time.Instant.class})
public interface CriteriaMapper {

    CriteriaMapper INSTANCE = Mappers.getMapper(CriteriaMapper.class);

    @IterableMapping(qualifiedByName="mapWithExpression")
    List<CriterionResponse> criteriaToDtos(List<Criterion> criteria);

    @Named("mapWithExpression")
    @Mapping(target = "imperative", expression = "java(criterion.getImperative().getId())")
    @Mapping(target = "typeCriterion", expression = "java(criterion.getTypeCriterion().getId())")
    CriterionResponse criteriaToDto(Criterion criterion);

    @Mapping(target = "cri", expression = "java(criteriaRequestDto.cri.toUpperCase())")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "populatedBy", ignore = true)
    @Mapping(target = "lastTouch", expression = "java(Timestamp.from(Instant.now()))")
    @Mapping(target = "lodCriP", ignore = true)
    @Mapping(target = "lodCriV", ignore = true)
    @Mapping(target = "imperative", ignore = true)
    @Mapping(target = "typeCriterion", ignore = true)
    @Mapping(target = "principles", ignore = true)
    @Mapping(target = "metrics", ignore = true)
    @Mapping(target = "actors", ignore = true)
    Criterion criteriaToEntity(CriterionRequest criteriaRequestDto);

    @Mapping(target = "cri", expression = "java(StringUtils.isNotEmpty(request.cri) ? request.cri : criterion.getCri())")
    @Mapping(target = "label", expression = "java(StringUtils.isNotEmpty(request.label) ? request.label : criterion.getLabel())")
    @Mapping(target = "description", expression = "java(StringUtils.isNotEmpty(request.description) ? request.description : criterion.getDescription())")
    @Mapping(target = "imperative", ignore = true)
    @Mapping(target = "typeCriterion", ignore = true)
    @Mapping(target = "lastTouch", expression = "java(Timestamp.from(Instant.now()))")
    @Mapping(target = "populatedBy", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lodCriP", ignore = true)
    @Mapping(target = "lodCriV", ignore = true)
    @Mapping(target = "principles", ignore = true)
    @Mapping(target = "metrics", ignore = true)
    @Mapping(target = "actors", ignore = true)
    void updateCriteria(CriterionUpdate request, @MappingTarget Criterion criterion);
}
