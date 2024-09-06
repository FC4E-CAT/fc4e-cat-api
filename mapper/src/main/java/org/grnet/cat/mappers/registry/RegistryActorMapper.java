package org.grnet.cat.mappers.registry;


import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.registry.codelist.ImperativeResponse;
import org.grnet.cat.dtos.registry.codelist.RegistryActorResponse;
import org.grnet.cat.entities.registry.Imperative;
import org.grnet.cat.entities.registry.RegistryActor;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The RegistryActorMapper is responsible for mapping RegistryActor entities to DTOs and vice versa.
 */
@Mapper(imports = {StringUtils.class, java.sql.Timestamp.class, java.time.Instant.class})
public interface RegistryActorMapper {

    RegistryActorMapper INSTANCE = Mappers.getMapper(RegistryActorMapper.class);

    @Named("map")
    RegistryActorResponse actorToDto(RegistryActor actor);

    @IterableMapping(qualifiedByName = "map")
    List<RegistryActorResponse> actorToDtos(List<RegistryActor> actorList);

}

