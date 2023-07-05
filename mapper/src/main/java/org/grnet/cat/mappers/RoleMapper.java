package org.grnet.cat.mappers;

import org.grnet.cat.dtos.RoleDto;
import org.grnet.cat.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The ActorMapper is responsible for mapping Role entities to DTOs and vice versa.
 */
@Mapper
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class );

    List<RoleDto> rolesToDto(List<Role> roles);
}
