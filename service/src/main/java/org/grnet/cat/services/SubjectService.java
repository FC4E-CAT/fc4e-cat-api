package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.assessment.AdminPartialJsonAssessmentResponse;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.dtos.subject.SubjectRequest;
import org.grnet.cat.dtos.subject.SubjectResponse;
import org.grnet.cat.dtos.subject.UpdateSubjectRequestDto;
import org.grnet.cat.entities.PageQuery;
import org.grnet.cat.entities.Subject;
import org.grnet.cat.exceptions.ConflictException;
import org.grnet.cat.mappers.AssessmentMapper;
import org.grnet.cat.mappers.SubjectMapper;
import org.grnet.cat.repositories.MotivationAssessmentRepository;
import org.grnet.cat.repositories.SubjectRepository;

import java.util.Optional;

/**
 * The SubjectService provides operations for managing Subject entities.
 */
@ApplicationScoped
public class SubjectService {


    @Inject
    MotivationAssessmentRepository assessmentRepository;

    @Inject
    SubjectRepository subjectRepository;

    /**
     * Creates a new subject.
     *
     * @param request The Subject to be created.
     * @param userId The user who requests to create the subject.
     * @return The created Subject.
     * @throws ConflictException  if a Subject exists for the user.
     */
    @Transactional
    public SubjectResponse createSubject(SubjectRequest request, String userId) {

        var subject = SubjectMapper.INSTANCE.dtoToSubject(request);
        subject.setCreatedBy(userId);

        var optional = subjectRepository.fetchSubjectByNameAndTypeAndSubjectId(request.name, request.type, request.id, userId);

        if(optional.isPresent()){

            throw new ConflictException(String.format("This subject {%s, %s, %s} has already been created.", request.id, request.name, request.type));
        }

        subjectRepository.persist(subject);

        return SubjectMapper.INSTANCE.subjectToDto(subject);
    }

    public Optional<Subject> getSubjectByNameAndTypeAndSubjectId(String name, String type, String subjectId, String userID){

        return subjectRepository.fetchSubjectByNameAndTypeAndSubjectId(name, type, subjectId, userID);
    }

    /**
     * Deletes a subject if it belongs to the authenticated user.
     *
     * @param userID The ID of the user who requests to delete the subject.
     * @param subjectId The ID of the subject to be deleted.
     * @throws ForbiddenException If the user does not have permission to delete this subject.
     */
    @Transactional
    public void deleteSubjectBelongsToUser(String userID, Long subjectId){

        var subject = subjectRepository.findById(subjectId);

        if (!subject.getCreatedBy().equals(userID)) {
            throw new ForbiddenException("User not authorized to manage subject with ID " + subjectId);
        }

        subjectRepository.delete(subject);
    }

    /**
     * Retrieves a page of Subjects submitted by the specified user.
     *
     * @param page The index of the page to retrieve (starting from 0).
     * @param size The maximum number of Subjects to include in a page.
     * @param uriInfo The Uri Info.
     * @param userID The ID of the user.
     * @return A list of SubjectResponse objects representing the submitted Subjects in the requested page.
     */
    public PageResource<SubjectResponse> getSubjectsByUserAndPage(int page, int size, UriInfo uriInfo, String userID){

        PageQuery<Subject> subjects = subjectRepository.fetchSubjectsByUserAndPage(page, size, userID);

        return new PageResource<>(subjects, SubjectMapper.INSTANCE.subjectsToDto(subjects.list()), uriInfo);
    }

    /**
     * Retrieves a specific Subject.
     *
     * @param id The ID of the Subject to retrieve.
     * @param userID The ID of the user who requests the subject.
     * @return The corresponding Subject.
     */
    public SubjectResponse getSubjectByUserAndId(Long id, String userID){

        var subject = subjectRepository.findById(id);

        if (!subject.getCreatedBy().equals(userID)) {
            throw new ForbiddenException("User not authorized to manage subject with ID " + id);
        }

        return SubjectMapper.INSTANCE.subjectToDto(subject);
    }

    /**
     * Retrieves a specific Subject.
     *
     * @param id The ID of the Subject to retrieve.
     * @return The corresponding Subject.
     */
    public Subject getSubjectById(Long id){

        var subject = subjectRepository.findByIdOptional(id);

        return subject.orElseThrow(()-> new NotFoundException("There is no Subject with the following id: "+id));
    }

    /**
     * This method updates a Subject.
     *
     * @param id The ID of the Subject to update.
     * @param request The Subject attributes to be updated.
     * @param userID The ID of the user who requests to update the subject.
     * @return The updated Subject.
     * @throws ConflictException  if a Subject exists for the user.
     */
    @Transactional
    public SubjectResponse update(Long id, UpdateSubjectRequestDto request, String userID){

        var subject = subjectRepository.findById(id);

        if (!subject.getCreatedBy().equals(userID)) {
            throw new ForbiddenException("User not authorized to manage subject with ID " + id);
        }

        SubjectMapper.INSTANCE.updateSubjectFromDto(request, subject);
        return SubjectMapper.INSTANCE.subjectToDto(subject);
    }

    public PageResource<AdminPartialJsonAssessmentResponse> getAssessmentsPerSubject(int page , int size, Long id, UriInfo uriInfo){

        var assessments = assessmentRepository.fetchAssessmentsPerSubjectAndPage(page, size, id);

        var fullAssessments = AssessmentMapper.INSTANCE.adminRegistryAssessmentsToJsonAssessments(assessments.list());

        return new PageResource<>(assessments, AssessmentMapper.INSTANCE.adminRegistryAssessmentsToPartialJsonAssessments(fullAssessments), uriInfo);

    }
}
