package org.grnet.cat.mappers.registry;

import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.registry.actor.MotivationActorResponse;
import org.grnet.cat.entities.registry.MotivationActorJunction;
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

}

