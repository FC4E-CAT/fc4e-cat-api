package org.grnet.cat.mappers;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.grnet.cat.dtos.assessment.AssessmentDoc;
import org.grnet.cat.dtos.assessment.JsonAssessmentResponse;
import org.grnet.cat.dtos.template.TemplateDto;
import org.grnet.cat.entities.Assessment;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * The AssessmentMapper is responsible for mapping Assessment entities to DTOs.
 */

@Mapper(imports = {Timestamp.class, Instant.class})
public interface AssessmentMapper {

    AssessmentMapper INSTANCE = Mappers.getMapper(AssessmentMapper.class);

    @IterableMapping(qualifiedByName = "mapWithExpression")
    List<JsonAssessmentResponse> assessmentsToJsonAssessments(List<Assessment> assessments);

    @Named("mapWithExpression")
    @Mapping(target = "assessmentDoc", expression = "java(stringJsonToDto(assessment.getAssessmentDoc()))")
    @Mapping(target = "templateId", expression = "java(assessment.getTemplate().getId())")
    @Mapping(target = "validationId", expression = "java(assessment.getValidation().getId())")
    @Mapping(target = "createdOn", expression = "java(assessment.getCreatedOn().toString())")
    @Mapping(target = "userId", expression = "java(assessment.getValidation().getUser().getId())")
    @Mapping(target = "updatedOn", expression = "java(assessment.getUpdatedOn() != null ? assessment.getUpdatedOn().toString() : \"\")")
    JsonAssessmentResponse assessmentToJsonAssessment(Assessment assessment);

    @Mapping(target = "id", ignore = true)
    AssessmentDoc templateDocToAssessmentDoc(TemplateDto template);

    @SneakyThrows
    default AssessmentDoc stringJsonToDto(String doc) {

        var objectMapper = new ObjectMapper();
        return objectMapper.readValue(doc, AssessmentDoc.class);

    }
}