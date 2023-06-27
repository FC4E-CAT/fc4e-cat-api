package org.grnet.cat.mappers;

import org.grnet.cat.dtos.ValidationResponse;
import org.grnet.cat.entities.Validation;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The ValidationMapper is responsible for mapping Validation entities to DTOs and vice versa.
 */
@Mapper
public interface ValidationMapper {

    ValidationMapper INSTANCE = Mappers.getMapper( ValidationMapper.class );

    @IterableMapping(qualifiedByName="mapWithExpression")
    List<ValidationResponse> validationsToDto(List<Validation> validations);

    @Named("mapWithExpression")
    @Mapping(target = "actorId", expression = "java(validation.getActor().getId())")
    ValidationResponse validationToDto(Validation validation);

}
