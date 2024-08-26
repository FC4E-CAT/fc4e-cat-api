package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.assessment.CommentRequestDto;
import org.grnet.cat.dtos.assessment.CommentResponseDto;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.entities.Assessment;
import org.grnet.cat.entities.User;
import org.grnet.cat.mappers.CommentMapper;
import org.grnet.cat.repositories.AssessmentRepository;
import org.grnet.cat.repositories.CommentRepository;
import org.grnet.cat.repositories.UserRepository;
import org.jboss.logging.Logger;
import io.quarkus.hibernate.orm.panache.Panache;
import java.time.LocalDateTime;

@ApplicationScoped
public class CommentService {

    @Inject
    CommentRepository commentRepository;
    private static final Logger LOG = Logger.getLogger(CommentService.class);

    /**
     * Adds a comment to the specified assessment.
     *
     * @param assessmentId The ID of the assessment.
     * @param commentRequestDto      The comment to add.
     * @return The added comment.
     */
    @Transactional
    public CommentResponseDto addCommentToAssessment(String assessmentId, String userId, CommentRequestDto commentRequestDto) {

        var assessment = Panache.getEntityManager().getReference(Assessment.class, assessmentId);
        var user = Panache.getEntityManager().getReference(User.class, userId);

        var comment = CommentMapper.INSTANCE.commentRequestToEntity(commentRequestDto);

        comment.setAssessment(assessment);
        comment.setUser(user);
        comment.setText(commentRequestDto.text);
        comment.setCreatedOn(LocalDateTime.now());

        commentRepository.persist(comment);

        return CommentMapper.INSTANCE.commentToDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto commentRequestDto) {

        var comment = commentRepository.findById(commentId);

        comment.setText(commentRequestDto.text);

        return CommentMapper.INSTANCE.commentToDto(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {

        commentRepository.deleteById(commentId);
    }

    @Transactional
    public void deleteAll() {
        commentRepository.deleteAll();
    }

    @Transactional
    public PageResource<CommentResponseDto> listComments(String assessmentId, int page, int size, UriInfo uriInfo) {

        var commentsPage = commentRepository.fetchCommentsByAssessmentId(assessmentId, page, size);
        var commentDTOs = CommentMapper.INSTANCE.commentsToDtos(commentsPage.list());

        return new PageResource<>(commentsPage, commentDTOs, uriInfo);
    }

}

