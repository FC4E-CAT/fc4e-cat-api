package org.grnet.cat.mappers;

import org.grnet.cat.dtos.ActorDto;
import org.grnet.cat.entities.Actor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The ActorMapper is responsible for mapping Actor entities to DTOs and vice versa.
 */
@Mapper
public interface ActorMapper {

    ActorMapper INSTANCE = Mappers.getMapper( ActorMapper.class );

    List<ActorDto> actorsToDto(List<Actor> actors);
}
