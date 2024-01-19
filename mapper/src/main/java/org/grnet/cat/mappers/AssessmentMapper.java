package org.grnet.cat.mappers;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.grnet.cat.dtos.assessment.AssessmentDoc;
import org.grnet.cat.dtos.assessment.JsonAssessmentResponse;
import org.grnet.cat.dtos.assessment.PartialJsonAssessmentResponse;
import org.grnet.cat.dtos.statistics.AssessmentPerActorDto;
import org.grnet.cat.dtos.template.TemplateDto;
import org.grnet.cat.entities.Assessment;
import org.grnet.cat.entities.AssessmentPerActor;
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

    @IterableMapping(qualifiedByName = "partialMapWithExpression")
    List<PartialJsonAssessmentResponse> assessmentsToPartialJsonAssessments(List<JsonAssessmentResponse> assessments);

    @Named("partialMapWithExpression")
    @Mapping(target = "name", expression = "java(assessment.assessmentDoc.name)")
    @Mapping(target = "type", expression = "java(assessment.assessmentDoc.assessmentType.name)")
    @Mapping(target = "actor", expression = "java(assessment.assessmentDoc.actor.name)")
    @Mapping(target = "organisation", expression = "java(assessment.assessmentDoc.organisation.name)")
    @Mapping(target = "published", expression = "java(assessment.assessmentDoc.published)")
    @Mapping(target = "subjectName", expression = "java(assessment.assessmentDoc.subject.name)")
    @Mapping(target = "subjectType", expression = "java(assessment.assessmentDoc.subject.type)")
    @Mapping(target = "compliance", expression = "java(assessment.assessmentDoc.result.compliance)")
    @Mapping(target = "ranking", expression = "java(assessment.assessmentDoc.result.ranking)")
    PartialJsonAssessmentResponse assessmentToPartialJsonAssessment(JsonAssessmentResponse assessment);


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

    @IterableMapping(qualifiedByName = "mapWithExpression")
    List<AssessmentPerActorDto> assessmentPerActorsToAssessmentPerActorsDto(List<AssessmentPerActor> assessmentPerActors);
    @Named("mapWithExpression")
    @Mapping(target = "totalAssessmentNum", expression = "java(assessmentPerActor.getTotal())")
    @Mapping(target = "actor", expression = "java(assessmentPerActor.getActor_name())")


    AssessmentPerActorDto assessmentPerActorToAssessmentPerActorDto(AssessmentPerActor assessmentPerActor);
}