package org.grnet.cat.mappers.registry;

import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.registry.actor.MotivationActorResponse;
import org.grnet.cat.dtos.registry.actor.PartialMotivationActorResponse;
import org.grnet.cat.dtos.registry.codelist.RegistryActorResponse;
import org.grnet.cat.dtos.registry.principle.PrinciplePartialResponse;
import org.grnet.cat.entities.registry.MotivationActorJunction;
import org.grnet.cat.entities.registry.PrincipleCriterionJunction;
import org.grnet.cat.entities.registry.RegistryActor;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The RegistryActorMapper is responsible for mapping RegistryActor entities to DTOs and vice versa.
 */
@Mapper(imports = {StringUtils.class, java.sql.Timestamp.class, java.time.Instant.class})
public interface MotivationActorMapper {

    MotivationActorMapper INSTANCE = Mappers.getMapper(MotivationActorMapper.class);

    @Named("map")

    @Mapping(target = "lodMAV", expression = "java(motivationActor.getId().getLodMAV())")
    @Mapping(target = "actor", expression = "java(motivationActor.getActor().getId())")
    @Mapping(target = "relation", expression = "java(motivationActor.getRelation().getId())")
    @Mapping(target = "motivationX", expression = "java(motivationActor.getMotivationX())")
    MotivationActorResponse fullMotivationActorToDto(MotivationActorJunction motivationActor);

    @IterableMapping(qualifiedByName = "map")
    List<MotivationActorResponse> fullMotivationActorToDtos(List<MotivationActorJunction> motivationActorList);

    @Named("partialMap")
//    @Mapping(source = "motivationActor.actor", target = "actor", qualifiedByName = "mapActor")
    @Mapping(target = "id", expression = "java(motivationActor.getActor().getId())")  // Ignore the id field here as well
    @Mapping(target = "act", expression = "java(motivationActor.getActor().getAct())")
    @Mapping(target = "labelActor", expression = "java(motivationActor.getActor().getLabelActor())")
    @Mapping(target = "uriActor", expression = "java(motivationActor.getActor().getUriActor())")
    @Mapping(target = "descActor", expression = "java(motivationActor.getActor().getDescActor())")
    @Mapping(target = "lodACTV", expression = "java(motivationActor.getActor().getLodACTV())")
    @Mapping(target = "principleCriterionCount", ignore = true)
    @Mapping(target = "existsPrincipleCriterion", ignore = true)
    @Mapping(target = "populatedBy", expression = "java(motivationActor.getActor().getPopulatedBy())")

    @Mapping(target = "published", expression = "java(motivationActor.getPublished())")
    PartialMotivationActorResponse partialMotivationActorToDto(MotivationActorJunction motivationActor);

    @IterableMapping(qualifiedByName = "partialMap")
    List<PartialMotivationActorResponse> partialMotivationActorToDtos(List<MotivationActorJunction> motivationActorList);

//    @Named("mapActor")
//    @Mapping(target = "id", expression = "java(actor.getId())")  // Ignore the id field here as well
//    @Mapping(target = "act", expression = "java(actor.getAct())")
//    @Mapping(target = "labelActor", expression = "java(actor.getLabelActor())")
//    @Mapping(target = "uriActor", expression = "java(actor.getUriActor())")
//    @Mapping(target = "descActor", expression = "java(actor.getDescActor())")
//    @Mapping(target = "lodACTV", expression = "java(actor.getLodACTV())")
//    @Mapping(target = "principleCriterionCount", ignore = true)
//    @Mapping(target = "existsPrincipleCriterion", ignore = true)
//    @Mapping(target = "populatedBy", expression = "java(actor.getPopulatedBy())")
//    RegistryActorResponse mapActor(RegistryActor actor);

}

