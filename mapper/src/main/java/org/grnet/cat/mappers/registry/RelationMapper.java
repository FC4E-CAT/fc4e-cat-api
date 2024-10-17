package org.grnet.cat.mappers.registry;

import org.apache.commons.lang3.StringUtils;
import org.grnet.cat.dtos.registry.RelationResponse;
import org.grnet.cat.dtos.registry.codelist.RegistryActorResponse;
import org.grnet.cat.entities.registry.RegistryActor;
import org.grnet.cat.entities.registry.Relation;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The RelationMapper is responsible for mapping Relation entities to DTOs and vice versa.
 */
@Mapper(imports = {StringUtils.class, java.sql.Timestamp.class, java.time.Instant.class})
public interface RelationMapper {

    RelationMapper INSTANCE = Mappers.getMapper(RelationMapper.class);

    @Named("map")
    RelationResponse relationToDto(Relation relation);

    @IterableMapping(qualifiedByName = "map")
    List<RelationResponse> relationToDtos(List<Relation> relationList);

}
