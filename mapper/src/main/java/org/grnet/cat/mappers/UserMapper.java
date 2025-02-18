package org.grnet.cat.mappers;

import org.grnet.cat.dtos.UserProfileDto;
import org.grnet.cat.dtos.UserRegistryAssessmentEligibilityResponse;
import org.grnet.cat.entities.Role;
import org.grnet.cat.entities.User;
import org.grnet.cat.entities.projections.UserRegistryAssessmentEligibility;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The UserMapper is responsible for mapping User entities to DTOs.
 */
@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );

    UserProfileDto userToProfileDto(User user);

    List<UserProfileDto> usersProfileToDto(List<User> users);

    @Named("registryMapWithExpression")
    UserRegistryAssessmentEligibilityResponse userRegistryAssessmentEligibilityToDto(UserRegistryAssessmentEligibility userRegistryAssessmentEligibility);

    @IterableMapping(qualifiedByName="registryMapWithExpression")
    List<UserRegistryAssessmentEligibilityResponse> listOfUserRegistryRegistryAssessmentEligibilityToDto(List<UserRegistryAssessmentEligibility> registryAssessmentEligibilities);


    default String mapRoleToString(Role role) {
        return role.getName();
    }
}
