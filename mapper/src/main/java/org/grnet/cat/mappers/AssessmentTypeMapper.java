package org.grnet.cat.mappers;


import org.grnet.cat.dtos.AssessmentTypeDto;
import org.grnet.cat.entities.AssessmentType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * The AssessmentTypeMapper is responsible for mapping AssessmentType entities to DTOs.
 */
@Mapper
public interface AssessmentTypeMapper {

    AssessmentTypeMapper INSTANCE = Mappers.getMapper(AssessmentTypeMapper.class);

    List<AssessmentTypeDto> assessmentTypesToDto(List<AssessmentType> assessmentTypes);
}