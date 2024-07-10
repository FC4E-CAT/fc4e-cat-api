package org.grnet.cat.mappers;

import org.grnet.cat.dtos.UserAssessmentEligibilityResponse;
import org.grnet.cat.dtos.UserProfileDto;
import org.grnet.cat.dtos.ValidationResponse;
import org.grnet.cat.entities.Role;
import org.grnet.cat.entities.User;
import org.grnet.cat.entities.Validation;
import org.grnet.cat.entities.projections.UserAssessmentEligibility;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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

    @Named("mapWithExpression")
    UserAssessmentEligibilityResponse userAssessmentEligibilityToDto(UserAssessmentEligibility userAssessmentEligibility);

    @IterableMapping(qualifiedByName="mapWithExpression")
    List<UserAssessmentEligibilityResponse> listOfUserAssessmentEligibilityToDto(List<UserAssessmentEligibility> listOfUserAssessmentEligibility);


    default String mapRoleToString(Role role) {
        return role.getName();
    }
}
