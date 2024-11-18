package org.grnet.cat.mappers;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.inject.spi.CDI;
import lombok.SneakyThrows;
import org.grnet.cat.dtos.assessment.AssessmentDoc;
import org.grnet.cat.dtos.assessment.AdminPartialJsonAssessmentResponse;
import org.grnet.cat.dtos.assessment.UserPartialJsonAssessmentResponse;
import org.grnet.cat.dtos.assessment.registry.AdminJsonRegistryAssessmentResponse;
import org.grnet.cat.dtos.assessment.registry.RegistryAssessmentDto;
import org.grnet.cat.dtos.assessment.registry.UserJsonRegistryAssessmentResponse;
import org.grnet.cat.dtos.statistics.AssessmentPerActorDto;
import org.grnet.cat.entities.AssessmentPerActor;
import org.grnet.cat.entities.MotivationAssessment;
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


    @IterableMapping(qualifiedByName = "partialMapWithRegistryExpression")
    List<UserPartialJsonAssessmentResponse> userRegistryAssessmentsToPartialJsonAssessments(List<UserJsonRegistryAssessmentResponse> assessments);

    @Named("partialMapWithRegistryExpression")
    @Mapping(target = "name", expression = "java(assessment.assessmentDoc.name)")
    @Mapping(target = "type", expression = "java(assessment.assessmentDoc.motivation.getName())")
    @Mapping(target = "actor", expression = "java(assessment.assessmentDoc.actor.getName())")
    @Mapping(target = "organisation", expression = "java(assessment.assessmentDoc.organisation.name)")
    @Mapping(target = "published", expression = "java(assessment.assessmentDoc.published)")
    @Mapping(target = "subjectName", expression = "java(assessment.assessmentDoc.subject.name)")
    @Mapping(target = "subjectType", expression = "java(assessment.assessmentDoc.subject.type)")
    @Mapping(target = "compliance", expression = "java(assessment.assessmentDoc.result.compliance)")
    @Mapping(target = "ranking", expression = "java(assessment.assessmentDoc.result.ranking)")
    UserPartialJsonAssessmentResponse userRegistryAssessmentToPartialJsonAssessment(UserJsonRegistryAssessmentResponse assessment);

    @Named("adminMapRegistryWithExpression")
    @Mapping(target = "assessmentDoc", expression = "java(registryStringJsonToDto(assessment.getAssessmentDoc()))")
    @Mapping(target = "validationId", expression = "java(assessment.getValidation().getId())")
    @Mapping(target = "createdOn", expression = "java(assessment.getCreatedOn().toString())")
    @Mapping(target = "userId", expression = "java(assessment.getValidation().getUser().getId())")
    @Mapping(target = "updatedOn", expression = "java(assessment.getUpdatedOn() != null ? assessment.getUpdatedOn().toString() : \"\")")
    @Mapping(target = "updatedBy", expression = "java(assessment.getUpdatedBy() != null ? assessment.getUpdatedBy() : \"\")")
    @Mapping(target = "shared", expression = "java(assessment.getShared())")
    AdminJsonRegistryAssessmentResponse adminRegistryAssessmentToJsonAssessment(MotivationAssessment assessment);

    @Named("adminPartialRegistryMapWithExpression")
    @Mapping(target = "name", expression = "java(assessment.assessmentDoc.name)")
    @Mapping(target = "type", expression = "java(assessment.assessmentDoc.motivation.getName())")
    @Mapping(target = "actor", expression = "java(assessment.assessmentDoc.actor.getName())")
    @Mapping(target = "organisation", expression = "java(assessment.assessmentDoc.organisation.name)")
    @Mapping(target = "published", expression = "java(assessment.assessmentDoc.published)")
    @Mapping(target = "subjectName", expression = "java(assessment.assessmentDoc.subject.name)")
    @Mapping(target = "subjectType", expression = "java(assessment.assessmentDoc.subject.type)")
    @Mapping(target = "compliance", expression = "java(assessment.assessmentDoc.result.compliance)")
    @Mapping(target = "ranking", expression = "java(assessment.assessmentDoc.result.ranking)")
    @Mapping(target = "shared", expression = "java(assessment.shared)")
    AdminPartialJsonAssessmentResponse adminRegistryAssessmentToPartialJsonAssessment(AdminJsonRegistryAssessmentResponse assessment);

    @IterableMapping(qualifiedByName = "adminPartialRegistryMapWithExpression")
    List<AdminPartialJsonAssessmentResponse> adminRegistryAssessmentsToPartialJsonAssessments(List<AdminJsonRegistryAssessmentResponse> assessments);


    @IterableMapping(qualifiedByName = "adminMapRegistryWithExpression")
    List<AdminJsonRegistryAssessmentResponse> adminRegistryAssessmentsToJsonAssessments(List<MotivationAssessment> assessments);

    @Named("mapWithRegistryExpression")
    @Mapping(target = "assessmentDoc", expression = "java(registryStringJsonToDto(assessment.getAssessmentDoc()))")
    @Mapping(target = "validationId", expression = "java(assessment.getValidation().getId())")
    @Mapping(target = "createdOn", expression = "java(assessment.getCreatedOn().toString())")
    @Mapping(target = "userId", expression = "java(assessment.getValidation().getUser().getId())")
    @Mapping(target = "updatedOn", expression = "java(assessment.getUpdatedOn() != null ? assessment.getUpdatedOn().toString() : \"\")")
    @Mapping(target = "updatedBy", expression = "java(assessment.getUpdatedBy() != null ? assessment.getUpdatedBy() : \"\")")
    @Mapping(target = "sharedToUser", expression = "java(isSharedToUser(assessment.getValidation().getUser().getId()))")
    @Mapping(target = "sharedByUser", expression = "java(isRegistryAssessmentSharedByUser(assessment, assessment.getValidation().getUser().getId()))")
    UserJsonRegistryAssessmentResponse userRegistryAssessmentToJsonAssessment(MotivationAssessment assessment);

    @IterableMapping(qualifiedByName = "mapWithRegistryExpression")
    List<UserJsonRegistryAssessmentResponse> userRegistryAssessmentsToJsonAssessments(List<MotivationAssessment> assessments);

    @IterableMapping(qualifiedByName = "mapWithExpression")
    List<AssessmentPerActorDto> assessmentPerActorsToAssessmentPerActorsDto(List<AssessmentPerActor> assessmentPerActors);

    @Named("mapWithExpression")
    @Mapping(target = "totalAssessmentNum", expression = "java(assessmentPerActor.getTotal())")
    @Mapping(target = "actor", expression = "java(assessmentPerActor.getActor_name())")
    AssessmentPerActorDto assessmentPerActorToAssessmentPerActorDto(AssessmentPerActor assessmentPerActor);

    default Boolean isRegistryAssessmentSharedByUser(MotivationAssessment assessment, String userId) {

        var utility = CDI.current().select(Utility.class).get();
        var currentUser = utility.getUserUniqueIdentifier();
        var sameUser = currentUser.equals(userId); //user logged is same as user owning the assessment

        return assessment.getShared() && sameUser; //if the assessment is shared and the user is the owner, the assessment is shared by the user
    }

    @SneakyThrows
    default AssessmentDoc stringJsonToDto(String doc) {

        var objectMapper = new ObjectMapper();
        return objectMapper.readValue(doc, AssessmentDoc.class);
    }

    @SneakyThrows
    default RegistryAssessmentDto registryStringJsonToDto(String doc) {

        var objectMapper = new ObjectMapper();
        return objectMapper.readValue(doc, RegistryAssessmentDto.class);
    }

    default Boolean isSharedToUser(String userId) {

        var utility = CDI.current().select(Utility.class).get();

        var currentUser = utility.getUserUniqueIdentifier();

        return !currentUser.equals(userId);
    }
}