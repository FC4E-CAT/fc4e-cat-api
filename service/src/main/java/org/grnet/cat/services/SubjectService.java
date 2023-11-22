package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.grnet.cat.dtos.subject.SubjectRequest;
import org.grnet.cat.dtos.subject.SubjectResponse;
import org.grnet.cat.exceptions.ConflictException;
import org.grnet.cat.mappers.SubjectMapper;
import org.grnet.cat.repositories.SubjectRepository;
import org.hibernate.exception.ConstraintViolationException;

/**
 * The SubjectService provides operations for managing Subject entities.
 */
@ApplicationScoped
public class SubjectService {

    @Inject
    SubjectRepository subjectRepository;

    /**
     * Creates a new subject.
     *
     * @param request The Subject to be created.
     * @param userId The user who requests to create the subject.
     * @return The created Subject.
     */
    @Transactional
    public SubjectResponse createSubject(SubjectRequest request, String userId) {

        var subject = SubjectMapper.INSTANCE.dtoToSubject(request);
        subject.setCreatedBy(userId);

        try
        {
            subjectRepository.persist(subject);
        }catch (ConstraintViolationException e){
            throw new ConflictException(String.format("This subject {%s, %s, %s} has already been created.", request.id, request.name, request.type));
        }

        return SubjectMapper.INSTANCE.subjectToDto(subject);
    }
}
