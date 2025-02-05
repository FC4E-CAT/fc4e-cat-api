package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.grnet.cat.dtos.statistics.*;
import org.grnet.cat.mappers.AssessmentMapper;
import org.grnet.cat.repositories.*;

@ApplicationScoped
@Named("statistics")
public class StatisticsService {

    @Inject
    RoleRepository roleRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    ValidationRepository validationRepository;

    @Inject
    MotivationAssessmentRepository assessmentRepository;

    @Inject
    SubjectRepository subjectRepository;


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

        var assessmentResponse = new AssessmentStatisticsResponse();
        assessmentResponse.assessmentsPerRole = assessmentsPerRole;
        assessmentResponse.totalAssessmentNum = total_assessments;
        assessmentResponse.privateAssessmentNum = private_count;
        assessmentResponse.publicAssessmentNum = public_count;

        //subject statistics
        var total_subjects = StatisticsEnum.Subject.TOTAL.getStatistics(subjectRepository);
        var subjectResponse = new SubjectStatisticsResponse();
        subjectResponse.totalSubjectNum = total_subjects;


        var response = new StatisticsResponse();
        response.assessmentStatistics=assessmentResponse;
        response.subjectStatistics=subjectResponse;
        response.userStatistics=userResponse;
        response.validationStatistics=validationResponse;
        return  response;
    }
}
