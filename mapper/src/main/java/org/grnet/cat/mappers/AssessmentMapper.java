package org.grnet.cat.mappers;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.inject.spi.CDI;
import lombok.SneakyThrows;
import org.grnet.cat.dtos.assessment.AssessmentDoc;
import org.grnet.cat.dtos.assessment.AdminPartialJsonAssessmentResponse;
import org.grnet.cat.dtos.assessment.UserPartialJsonAssessmentResponse;
import org.grnet.cat.dtos.assessment.registry.AdminJsonRegistryAssessmentResponse;
import org.grnet.cat.dtos.assessment.registry.CriNodeDto;
import org.grnet.cat.dtos.assessment.registry.RegistryAssessmentDto;
import org.grnet.cat.dtos.assessment.registry.UserJsonRegistryAssessmentResponse;
import org.grnet.cat.dtos.statistics.AssessmentPerActorDto;
import org.grnet.cat.entities.AssessmentPerActor;
import org.grnet.cat.entities.MotivationAssessment;
import org.grnet.cat.repositories.registry.TestDefinitionRepository;
import org.grnet.cat.repositories.registry.TestRepository;
import org.grnet.cat.utils.TestParamsTransformer;
import org.grnet.cat.utils.Utility;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    @Mapping(target = "published", expression = "java(assessment.getPublished())")
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
    @Mapping(target = "published", expression = "java(assessment.getPublished())")
    AdminJsonRegistryAssessmentResponse adminRegistryAssessmentToJsonAssessment(MotivationAssessment assessment);

    @Named("adminPartialRegistryMapWithExpression")
    @Mapping(target = "name", expression = "java(assessment.assessmentDoc.name)")
    @Mapping(target = "type", expression = "java(assessment.assessmentDoc.motivation.getName())")
    @Mapping(target = "actor", expression = "java(assessment.assessmentDoc.actor.getName())")
    @Mapping(target = "organisation", expression = "java(assessment.assessmentDoc.organisation.name)")
    @Mapping(target = "published", expression = "java(assessment.getPublished())")
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
    @Mapping(target = "published", source = "assessment.published")
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
        System.out.println("share is current user : "+currentUser);
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

        System.out.println("user is is : "+userId);
        var utility = CDI.current().select(Utility.class).get();
        var currentUser = utility.getUserUniqueIdentifier();
        System.out.println("get current user is : "+currentUser);

        return !currentUser.equals(userId);
    }

    @Named("publicMapWithRegistryExpression")
    @Mapping(target = "assessmentDoc", expression = "java(registryStringJsonToDto(assessment.getAssessmentDoc()))")
    @Mapping(target = "validationId", expression = "java(assessment.getValidation().getId())")
    @Mapping(target = "createdOn", expression = "java(assessment.getCreatedOn().toString())")
    @Mapping(target = "userId", expression = "java(assessment.getValidation().getUser().getId())")
    @Mapping(target = "updatedOn", expression = "java(assessment.getUpdatedOn() != null ? assessment.getUpdatedOn().toString() : \"\")")
    @Mapping(target = "updatedBy", expression = "java(assessment.getUpdatedBy() != null ? assessment.getUpdatedBy() : \"\")")
    @Mapping(target = "sharedToUser", ignore = true)
    @Mapping(target = "sharedByUser", ignore = true)
    @Mapping(target = "published", source = "assessment.published")
    UserJsonRegistryAssessmentResponse publicUserRegistryAssessmentToJsonAssessment(MotivationAssessment assessment);

    @Named("mapZenodoWithRegistryExpression")
    @Mapping(target = "assessmentDoc", expression = "java(registryStringJsonToDto(assessment.getAssessmentDoc()))")
    @Mapping(target = "validationId", expression = "java(assessment.getValidation().getId())")
    @Mapping(target = "createdOn", expression = "java(assessment.getCreatedOn().toString())")
    @Mapping(target = "userId", expression = "java(assessment.getValidation().getUser().getId())")
    @Mapping(target = "updatedOn", expression = "java(assessment.getUpdatedOn() != null ? assessment.getUpdatedOn().toString() : \"\")")
    @Mapping(target = "updatedBy", expression = "java(assessment.getUpdatedBy() != null ? assessment.getUpdatedBy() : \"\")")
    @Mapping(target = "sharedToUser", expression = "java(!uniqueIdentifier.equals(assessment.getValidation().getUser().getId()))")
    @Mapping(target = "sharedByUser", expression = "java(assessment.getShared() && assessment.getValidation() != null && assessment.getValidation().getUser() != null && uniqueIdentifier.equals(assessment.getValidation().getUser().getId()))")
    @Mapping(target = "published", source = "assessment.published")
    UserJsonRegistryAssessmentResponse zenodoUserRegistryAssessmentToJsonAssessment(MotivationAssessment assessment,String  uniqueIdentifier);

    @Named("jsonWithLatestChanges")
    @Mapping(target = "assessmentDoc", expression = "java(assessmentWithLatestChanges(assessment))")
    @Mapping(target = "validationId", expression = "java(assessment.getValidation().getId())")
    @Mapping(target = "createdOn", expression = "java(assessment.getCreatedOn().toString())")
    @Mapping(target = "userId", expression = "java(assessment.getValidation().getUser().getId())")
    @Mapping(target = "updatedOn", expression = "java(assessment.getUpdatedOn() != null ? assessment.getUpdatedOn().toString() : \"\")")
    @Mapping(target = "updatedBy", expression = "java(assessment.getUpdatedBy() != null ? assessment.getUpdatedBy() : \"\")")
    @Mapping(target = "sharedToUser", expression = "java(isSharedToUser(assessment.getValidation().getUser().getId()))")
    @Mapping(target = "sharedByUser", expression = "java(isRegistryAssessmentSharedByUser(assessment, assessment.getValidation().getUser().getId()))")
    @Mapping(target = "published", source = "assessment.published")
    UserJsonRegistryAssessmentResponse userRegistryAssessmentToJsonUpdatedWithLatestChangesAssessment(MotivationAssessment assessment);

    @SneakyThrows
    default RegistryAssessmentDto assessmentWithLatestChanges(MotivationAssessment assessment) {

        var objectMapper = new ObjectMapper();

        var testRepository = CDI.current().select(TestRepository.class).get();

        var tests = testRepository.getUpdatedTests(assessment.getCreatedOn());

        var json = objectMapper.readValue(assessment.getAssessmentDoc(), RegistryAssessmentDto.class);

        if(!tests.isEmpty()){

            var jsonTests = json
                    .principles
                    .stream()
                    .flatMap(pri->pri.criteria.stream())
                    .map(CriNodeDto::getMetric)
                    .flatMap(metric->metric.getTests().stream())
                    .collect(Collectors.toList());

            jsonTests
                    .forEach(jsonTest->{

                        var optional =  tests
                                .stream()
                                .filter(obj -> obj.getTES().equals(jsonTest.getId()))
                                .findFirst();

                        if(optional.isPresent()){

                            var dbTest = optional.get();

                            jsonTest.setDescription(dbTest.getDescTest().trim());
                            jsonTest.setName(dbTest.getLabelTest().trim());
                            jsonTest.setResult(null);
                            jsonTest.setValue(null);

                            var testDefinitionRepository = CDI.current().select(TestDefinitionRepository.class).get();
                            var testDefinition = testDefinitionRepository.fetchTestDefinitionByTestId(dbTest.getId());
                            jsonTest.setParams(TestParamsTransformer.transformTestParams(testDefinition.getTestParams()));
                            jsonTest.setToolTip(testDefinition.getToolTip().trim());
                            jsonTest.setText(testDefinition.getTestQuestion().trim());
                            jsonTest.setChanged(Boolean.TRUE);

                            if(!Objects.isNull(jsonTest.getUrls())){

                                jsonTest.setUrls(new ArrayList<>());
                            }
                        }
                    });
        }

        return json;
    }
}