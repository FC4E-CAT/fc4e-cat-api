package org.grnet.cat.mappers.registry;

import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.registry.actor.MotivationActorResponse;
import org.grnet.cat.dtos.registry.criterion.CriterionActorResponse;
import org.grnet.cat.dtos.registry.criterion.CriterionRequest;
import org.grnet.cat.entities.registry.Criterion;
import org.grnet.cat.entities.registry.CriterionActorJunction;
import org.grnet.cat.entities.registry.MotivationActorJunction;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The CriterionActorMapper is responsible for mapping CriterionActor entities to DTOs and vice versa.
 */
@Mapper(imports = {StringUtils.class, java.sql.Timestamp.class, java.time.Instant.class})
public interface CriterionActorMapper {

    CriterionActorMapper INSTANCE = Mappers.getMapper(CriterionActorMapper.class);

    @Named("map")

    @Mapping(target = "lodCAV", expression = "java(criterionActor.getId().getLodCAV())")
    @Mapping(target = "criterion", expression = "java(criterionActor.getCriterion().getId())")
    @Mapping(target = "imperative", expression = "java(criterionActor.getImperative().getId())")
    @Mapping(target = "motivation", expression = "java(criterionActor.getMotivation().getId())")
    @Mapping(target = "motivationX", expression = "java(criterionActor.getMotivationX())")
    CriterionActorResponse criterionActorActorToDto(CriterionActorJunction criterionActor);

    @IterableMapping(qualifiedByName = "map")
    List<CriterionActorResponse> criterionActorToDtos(List<CriterionActorJunction> criterionActorList);


}

