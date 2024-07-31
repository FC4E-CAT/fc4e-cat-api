package org.grnet.cat.mappers;

import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.criteria.CriteriaRequestDto;
import org.grnet.cat.dtos.criteria.CriteriaResponseDto;
import org.grnet.cat.dtos.criteria.CriteriaUpdateDto;
import org.grnet.cat.entities.Criteria;
import org.grnet.cat.enums.CriteriaImperative;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The GuidanceMapper is responsible for mapping Criteria entities to DTOs and vice versa.
 */
@Mapper(imports = StringUtils.class)
public interface CriteriaMapper {

    CriteriaMapper INSTANCE = Mappers.getMapper(CriteriaMapper.class);

    @IterableMapping(qualifiedByName="mapWithExpression")
    List<CriteriaResponseDto> criteriaToDtos(List<Criteria> criteria);

    @Named("mapWithExpression")
    @Mapping(target = "imperative", source = "imperative", qualifiedByName = "mapImperativeToString")
    CriteriaResponseDto criteriaToDto(Criteria guidance);

    @Mapping(target = "uuid", expression = "java(criteriaRequestDto.uuid.toUpperCase())")
    @Mapping(target = "criteriaCode", expression = "java(criteriaRequestDto.criteriaCode.toUpperCase())")
    @Mapping(target = "imperative", source = "criteriaRequestDto.imperative", qualifiedByName = "mapStringToImperative")
    Criteria criteriaToEntity(CriteriaRequestDto criteriaRequestDto);

    @Mapping(target = "uuid", expression = "java(StringUtils.isNotEmpty(request.uuid) ? request.uuid : criteria.getUuid())")
    @Mapping(target = "criteriaCode", expression = "java(StringUtils.isNotEmpty(request.criteriaCode) ? request.criteriaCode : criteria.getCriteriaCode())")
    @Mapping(target = "label", expression = "java(StringUtils.isNotEmpty(request.label) ? request.label : criteria.getLabel())")
    @Mapping(target = "description", expression = "java(StringUtils.isNotEmpty(request.description) ? request.description : criteria.getDescription())")
    @Mapping(target = "imperative", expression = "java(StringUtils.isNotEmpty(request.imperative) ? mapStringToImperative(request.imperative) : criteria.getImperative())")
    void updateCriteria(CriteriaUpdateDto request, @MappingTarget Criteria criteria);

    @Named("mapStringToImperative")
    default CriteriaImperative mapStringToImperative(String imperative) {
        return CriteriaImperative.fromString(imperative);
    }

    @Named("mapImperativeToString")
    default String mapImperativeToString(CriteriaImperative imperative) {
        return imperative.toString();
    }
}
