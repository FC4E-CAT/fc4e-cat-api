package org.grnet.cat.mappers;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.inject.spi.CDI;
import lombok.SneakyThrows;
import org.grnet.cat.dtos.assessment.AssessmentDoc;
import org.grnet.cat.dtos.assessment.AdminJsonAssessmentResponse;
import org.grnet.cat.dtos.assessment.AdminPartialJsonAssessmentResponse;
import org.grnet.cat.dtos.assessment.UserJsonAssessmentResponse;
import org.grnet.cat.dtos.assessment.UserPartialJsonAssessmentResponse;
import org.grnet.cat.dtos.statistics.AssessmentPerActorDto;
import org.grnet.cat.dtos.template.TemplateDto;
import org.grnet.cat.entities.Assessment;
import org.grnet.cat.entities.AssessmentPerActor;
import org.grnet.cat.utils.Utility;
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
    List<UserPartialJsonAssessmentResponse> userAssessmentsToPartialJsonAssessments(List<UserJsonAssessmentResponse> assessments);

    @IterableMapping(qualifiedByName = "adminPartialMapWithExpression")
    List<AdminPartialJsonAssessmentResponse> adminAssessmentsToPartialJsonAssessments(List<AdminJsonAssessmentResponse> assessments);

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
    UserPartialJsonAssessmentResponse userAssessmentToPartialJsonAssessment(UserJsonAssessmentResponse assessment);

    @Named("adminPartialMapWithExpression")
    @Mapping(target = "name", expression = "java(assessment.assessmentDoc.name)")
    @Mapping(target = "type", expression = "java(assessment.assessmentDoc.assessmentType.name)")
    @Mapping(target = "actor", expression = "java(assessment.assessmentDoc.actor.name)")
    @Mapping(target = "organisation", expression = "java(assessment.assessmentDoc.organisation.name)")
    @Mapping(target = "published", expression = "java(assessment.assessmentDoc.published)")
    @Mapping(target = "subjectName", expression = "java(assessment.assessmentDoc.subject.name)")
    @Mapping(target = "subjectType", expression = "java(assessment.assessmentDoc.subject.type)")
    @Mapping(target = "compliance", expression = "java(assessment.assessmentDoc.result.compliance)")
    @Mapping(target = "ranking", expression = "java(assessment.assessmentDoc.result.ranking)")
    AdminPartialJsonAssessmentResponse adminAssessmentToPartialJsonAssessment(AdminJsonAssessmentResponse assessment);


    @IterableMapping(qualifiedByName = "mapWithExpression")
    List<UserJsonAssessmentResponse> userAssessmentsToJsonAssessments(List<Assessment> assessments);

    @IterableMapping(qualifiedByName = "adminMapWithExpression")
    List<AdminJsonAssessmentResponse> adminAssessmentsToJsonAssessments(List<Assessment> assessments);


    @Named("mapWithExpression")
    @Mapping(target = "assessmentDoc", expression = "java(stringJsonToDto(assessment.getAssessmentDoc()))")
    @Mapping(target = "templateId", expression = "java(assessment.getTemplate().getId())")
    @Mapping(target = "validationId", expression = "java(assessment.getValidation().getId())")
    @Mapping(target = "createdOn", expression = "java(assessment.getCreatedOn().toString())")
    @Mapping(target = "userId", expression = "java(assessment.getValidation().getUser().getId())")
    @Mapping(target = "updatedOn", expression = "java(assessment.getUpdatedOn() != null ? assessment.getUpdatedOn().toString() : \"\")")
    @Mapping(target = "updatedBy", expression = "java(assessment.getUpdatedBy() != null ? assessment.getUpdatedBy() : \"\")")
    @Mapping(target = "sharedToUser", expression = "java(isSharedToUser(assessment.getValidation().getUser().getId()))")
    UserJsonAssessmentResponse userAssessmentToJsonAssessment(Assessment assessment);

    @Named("adminMapWithExpression")
    @Mapping(target = "assessmentDoc", expression = "java(stringJsonToDto(assessment.getAssessmentDoc()))")
    @Mapping(target = "templateId", expression = "java(assessment.getTemplate().getId())")
    @Mapping(target = "validationId", expression = "java(assessment.getValidation().getId())")
    @Mapping(target = "createdOn", expression = "java(assessment.getCreatedOn().toString())")
    @Mapping(target = "userId", expression = "java(assessment.getValidation().getUser().getId())")
    @Mapping(target = "updatedOn", expression = "java(assessment.getUpdatedOn() != null ? assessment.getUpdatedOn().toString() : \"\")")
    @Mapping(target = "updatedBy", expression = "java(assessment.getUpdatedBy() != null ? assessment.getUpdatedBy() : \"\")")
    AdminJsonAssessmentResponse adminAssessmentToJsonAssessment(Assessment assessment);

    @Mapping(target = "id", ignore = true)
    AssessmentDoc templateDocToAssessmentDoc(TemplateDto template);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assessmentType", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "timestamp", ignore = true)
    @Mapping(target = "actor", ignore = true)
    @Mapping(target = "organisation", ignore = true)
    @Mapping(target = "subject", ignore = true)
    AssessmentDoc updateAssessmentDocFromTemplateDoc(TemplateDto template);

    @SneakyThrows
    default AssessmentDoc stringJsonToDto(String doc) {

        var objectMapper = new ObjectMapper();
        return objectMapper.readValue(doc, AssessmentDoc.class);
    }

    default Boolean isSharedToUser(String userId){

        var utility = CDI.current().select(Utility.class).get();

        var currentUser = utility.getUserUniqueIdentifier();

        return !currentUser.equals(userId);
    }

    @IterableMapping(qualifiedByName = "mapWithExpression")
    List<AssessmentPerActorDto> assessmentPerActorsToAssessmentPerActorsDto(List<AssessmentPerActor> assessmentPerActors);
    @Named("mapWithExpression")
    @Mapping(target = "totalAssessmentNum", expression = "java(assessmentPerActor.getTotal())")
    @Mapping(target = "actor", expression = "java(assessmentPerActor.getActor_name())")
    AssessmentPerActorDto assessmentPerActorToAssessmentPerActorDto(AssessmentPerActor assessmentPerActor);
}