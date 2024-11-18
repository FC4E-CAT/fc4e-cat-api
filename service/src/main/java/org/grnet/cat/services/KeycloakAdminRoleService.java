package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.RoleDto;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.statistics.*;
import org.grnet.cat.mappers.AssessmentMapper;
import org.grnet.cat.mappers.RoleMapper;
import org.grnet.cat.repositories.*;

import java.util.List;

/**
 * The KeycloakAdminRoleService class provides methods to connect and interact with the Keycloak admin API.
 */
@ApplicationScoped
@Named("keycloak-service")
public class KeycloakAdminRoleService implements RoleService {

    @Inject
    @Named("keycloak-repository")
    RoleRepository roleRepository;
    @Inject
    ValidationRepository validationRepository;
    @Inject
    MotivationAssessmentRepository assessmentRepository;
    @Inject
    SubjectRepository subjectRepository;

    @Inject
    UserRepository userRepository;
    /**
     * Retrieves a page of roles from the keycloak.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of roles to include in a page.
     * @param uriInfo The Uri Info.
     * @return A list of Role objects representing the roles in the requested page.
     */
    public PageResource<RoleDto> getRolesByPage(int page, int size, UriInfo uriInfo){

        var roles = roleRepository.fetchRolesByPage(page, size);

        return new PageResource<>(roles, RoleMapper.INSTANCE.rolesToDto(roles.list()), uriInfo);
    }

    /**
     * Assigns new roles to a specific user.
     *
     * @param userId The unique identifier of the user.
     * @param roles  List of role names to be assigned to the user.
     */
    @Override
    public void assignRolesToUser(String userId, List<String> roles) {

        roleRepository.doRolesExist(roles);
        roleRepository.assignRoles(userId, roles);
    }


    /**
     * Retrieves the statistics from the database.
     *
     * @return A StatisticsResponse object representing the statistics.
     */
    public StatisticsResponse getStatistics(){
        //user statistics
        var total_users = StatisticsEnum.User.TOTAL.getStatistics(roleRepository,userRepository);
        var identified_count = StatisticsEnum.User.IDENTIFIED.getStatistics(roleRepository,userRepository);
        var validated_count = StatisticsEnum.User.VALIDATED.getStatistics(roleRepository,userRepository);
        var banned_count = StatisticsEnum.User.BANNED.getStatistics(roleRepository,userRepository);

        var userResponse= new UserStatisticsResponse();
        userResponse.validatedUserNum=validated_count;
        userResponse.totalUsersNum=total_users;
        userResponse.identifiedUserNum=identified_count;
        userResponse.bannedUserNum=banned_count;


        //validation statistics

        var total_validations = StatisticsEnum.Validation.TOTAL.getStatistics(validationRepository);
        var accepted_count = StatisticsEnum.Validation.ACCEPTED.getStatistics(validationRepository);
        var pending_count = StatisticsEnum.Validation.PENDING.getStatistics(validationRepository);
        var validationResponse= new ValidationStatisticsResponse();
        validationResponse.totalValidationNum=total_validations;
        validationResponse.acceptedValidationNum=accepted_count;
        validationResponse.pendingValidationNum=pending_count;

      //assessment statistics
        var total_assessments = StatisticsEnum.Assessment.TOTAL.getStatistics(assessmentRepository);
        var public_count = StatisticsEnum.Assessment.PUBLIC.getStatistics(assessmentRepository);
        var private_count = StatisticsEnum.Assessment.PRIVATE.getStatistics(assessmentRepository);
        var numAssessmentsPerRole = assessmentRepository.getStatistics();

        var assessmentsPerRole = AssessmentMapper.INSTANCE.assessmentPerActorsToAssessmentPerActorsDto(numAssessmentsPerRole);

        AssessmentStatisticsResponse assessmentResponse = new AssessmentStatisticsResponse();
        assessmentResponse.assessmentsPerRole = assessmentsPerRole;
        assessmentResponse.totalAssessmentNum = total_assessments;
        assessmentResponse.privateAssessmentNum = private_count;
        assessmentResponse.publicAssessmentNum = public_count;

        //subject statistics
        var total_subjects = StatisticsEnum.Subject.TOTAL.getStatistics(subjectRepository);

        SubjectStatisticsResponse subjectResponse = new SubjectStatisticsResponse();
        subjectResponse.totalSubjectNum = total_subjects;


        StatisticsResponse response=new StatisticsResponse();
        response.assessmentStatistics=assessmentResponse;
        response.subjectStatistics=subjectResponse;
        response.userStatistics=userResponse;
        response.validationStatistics=validationResponse;
        return  response;
    }
}
