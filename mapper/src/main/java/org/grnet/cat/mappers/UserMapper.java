package org.grnet.cat.mappers;

import org.grnet.cat.dtos.UserProfileDto;
import org.grnet.cat.entities.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The UserMapper is responsible for mapping User entities to DTOs.
 */
@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );

    UserProfileDto userProfileToDto(UserProfile userProfile);

    List<UserProfileDto> usersProfileToDto(List<UserProfile> users);
}
