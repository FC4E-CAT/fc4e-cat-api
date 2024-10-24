package org.grnet.cat.mappers.registry;

import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.registry.criterion.CriterionActorResponse;
import org.grnet.cat.dtos.registry.criterion.PrincipleCriterionResponse;
import org.grnet.cat.dtos.registry.principle.PrinciplePartialResponse;
import org.grnet.cat.entities.registry.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The CriterionActorMapper is responsible for mapping CriterionActor entities to DTOs and vice versa.
 */
@Mapper(imports = {StringUtils.class, Timestamp.class, Instant.class})
public interface CriterionActorMapper {

    CriterionActorMapper INSTANCE = Mappers.getMapper(CriterionActorMapper.class);
    @Mapping(source = "criterion.id", target = "id")
    @Mapping(source = "criterion.cri", target = "cri")
    @Mapping(source = "criterion.label", target = "label")
    @Mapping(source = "criterion.description", target = "description")
    @Mapping(source = "criterion.typeCriterion.id", target = "typeCriterion")
    @Mapping(source = "criterion.url", target = "url")
    @Mapping(source = "criterion.lodCriP", target = "lodCriP")
    @Mapping(source = "populatedBy", target = "populatedBy")
    @Mapping(source = "lastTouch", target = "lastTouch")
    @Mapping(target = "principles", source = "criterion.principles", qualifiedByName = "mapPrinciples")
    PrincipleCriterionResponse toPrincipleCriterionResponse(CriterionActorJunction junction);

    List<PrincipleCriterionResponse> toPrincipleCriterionResponseList(List<CriterionActorJunction> junctions);

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

    @Mapping(source = "criterion.id", target = "id")
    @Mapping(source = "criterion.cri", target = "cri")
    @Mapping(source = "criterion.label", target = "label")
    @Mapping(source = "criterion.description", target = "description")
    @Mapping(source = "criterion.typeCriterion.id", target = "typeCriterion")
    @Mapping(source = "criterion.url", target = "url")
    @Mapping(source = "criterion.lodCriP", target = "lodCriP")
    @Mapping(source = "actor.labelActor", target = "actorLabel")
    @Mapping(source = "motivation.mtv", target = "motivationMtv")
    @Mapping(source = "populatedBy", target = "populatedBy")
    @Mapping(source = "lastTouch", target = "lastTouch")
    @Mapping(target = "principles", source = "criterion.principles", qualifiedByName = "mapPrinciples")
    CriterionActorResponse toCriterionActorResponse(CriterionActorJunction junction);

    List<CriterionActorResponse> toCriterionActorResponseList(List<CriterionActorJunction> junctions);

}

