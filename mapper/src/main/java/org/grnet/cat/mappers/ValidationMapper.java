package org.grnet.cat.mappers;

import org.grnet.cat.dtos.ValidationResponse;
import org.grnet.cat.entities.Validation;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Objects;

/**
 * The ValidationMapper is responsible for mapping Validation entities to DTOs and vice versa.
 */
@Mapper(imports = {Objects.class})
public interface ValidationMapper {

    ValidationMapper INSTANCE = Mappers.getMapper( ValidationMapper.class );

    @IterableMapping(qualifiedByName="mapWithExpression")
    List<ValidationResponse> validationsToDto(List<Validation> validations);

    @Named("mapWithExpression")
    @Mapping(target = "userId", expression = "java(validation.getUser().getId())")
    @Mapping(target = "userName", expression = "java(validation.getUser().getName())")
    @Mapping(target = "userSurname", expression = "java(validation.getUser().getSurname())")
    @Mapping(target = "userEmail", expression = "java(validation.getUser().getEmail())")
    @Mapping(target = "registryActorId", expression = "java(validation.getRegistryActor().getId())")
    @Mapping(target = "registryActorName", expression = "java(validation.getRegistryActor().getLabelActor())")
    ValidationResponse validationToDto(Validation validation);
}
