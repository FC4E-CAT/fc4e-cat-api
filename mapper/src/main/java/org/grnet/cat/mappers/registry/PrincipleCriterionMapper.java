package org.grnet.cat.mappers.registry;

import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.registry.CriterionMetricResponseDto;
import org.grnet.cat.dtos.registry.MetricTestResponseDto;
import org.grnet.cat.dtos.registry.PrincipleCriterionResponseDto;
import org.grnet.cat.dtos.registry.criterion.PrincipleCriterionResponse;
import org.grnet.cat.dtos.registry.principle.PrinciplePartialResponse;
import org.grnet.cat.dtos.registry.principle.PrincipleResponseDto;
import org.grnet.cat.entities.registry.*;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The PrincipleCriterionMapper is responsible for mapping Criteria entities to DTOs and vice versa.
 */
@Mapper(imports = {StringUtils.class, java.sql.Timestamp.class, java.time.Instant.class}, uses = ImperativeMapper.class)
public interface PrincipleCriterionMapper {

    PrincipleCriterionMapper INSTANCE = Mappers.getMapper(PrincipleCriterionMapper.class);

    @IterableMapping(qualifiedByName = "mapWithExpression")
    List<PrincipleCriterionResponse> criteriaToDtos(List<Criterion> criteria);

    //@IterableMapping(qualifiedByName = "mapWithExpression")
    List<PrincipleResponseDto> principleToDtos(List<Principle> principle);

    List<PrincipleCriterionResponseDto> principleCriterionToResponseDtos(List<PrincipleCriterionJunction> entities);

    @Named("mapWithExpression")
    @Mapping(target = "id", expression = "java(criterion.getId())")
    //@Mapping(target = "imperative", expression = "java(criterion.getImperative().getId())")
    @Mapping(target = "imperative", source = "imperative", qualifiedByName = "mapPartial")
    @Mapping(target = "typeCriterion", expression = "java(criterion.getTypeCriterion().getId())")
    @Mapping(source = "principles", target = "principles", qualifiedByName = "mapPrinciples")
    PrincipleCriterionResponse criteriaToDto(Criterion criterion);

    @Named("mapPrinciples")
    default List<PrinciplePartialResponse> mapPrinciples(Set<PrincipleCriterionJunction> principles) {
        return principles.stream()
                .map(this::mapPrinciple)
                .distinct()
                .collect(Collectors.toList());
    }

    @Named("mapPrinciple")
    @Mapping(target = "id",  expression = "java(principleJunction.getPrinciple().getId())")  // Ignore the id field here as well
    @Mapping(target = "pri", expression = "java(principleJunction.getPrinciple().getPri())")
    @Mapping(target = "label", expression = "java(principleJunction.getPrinciple().getLabel())")

    PrinciplePartialResponse mapPrinciple(PrincipleCriterionJunction principleJunction);

    @Mapping(target = "principleId", expression = "java(principleCriterion.getPrinciple().getId())")
    @Mapping(target = "criterionId", expression = "java(principleCriterion.getCriterion().getId())")
    @Mapping(target = "motivationId", expression = "java(principleCriterion.getMotivation().getId())")
    @Mapping(target = "relationId", expression = "java(principleCriterion.getRelation().getId())")
    PrincipleCriterionResponseDto principleCriterionToDto(PrincipleCriterionJunction principleCriterion);
}
