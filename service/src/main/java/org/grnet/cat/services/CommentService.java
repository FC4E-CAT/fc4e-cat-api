
package org.grnet.cat.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.assessment.CommentRequestDto;
import org.grnet.cat.dtos.assessment.CommentResponseDto;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.entities.Assessment;
import org.grnet.cat.entities.User;
import org.grnet.cat.enums.ShareableEntityType;
import org.grnet.cat.mappers.CommentMapper;
import org.grnet.cat.repositories.CommentRepository;
import org.grnet.cat.services.interceptors.ShareableEntity;
import org.jboss.logging.Logger;
import io.quarkus.hibernate.orm.panache.Panache;

import java.sql.Timestamp;
import java.time.Instant;
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

    @ShareableEntity(type= ShareableEntityType.ASSESSMENT, id = String.class)
    @Transactional
    public CommentResponseDto addComment(String assessmentId, String userId, CommentRequestDto commentRequestDto) {

        var assessment = Panache.getEntityManager().getReference(Assessment.class, assessmentId);
        var user = Panache.getEntityManager().getReference(User.class, userId);

        var comment = CommentMapper.INSTANCE.commentRequestToEntity(commentRequestDto);

        comment.setAssessment(assessment);
        comment.setUser(user);
        comment.setText(commentRequestDto.text);
        comment.setCreatedOn(Timestamp.from(Instant.now()));

        commentRepository.persist(comment);

        return CommentMapper.INSTANCE.commentToDto(comment);
    }

    @ShareableEntity(type= ShareableEntityType.ASSESSMENT, id = String.class)
    @Transactional
    public CommentResponseDto updateComment(String assessmentId, Long commentId, CommentRequestDto commentRequestDto, String userId) {

        var comment = commentRepository.findById(commentId);

        if (comment == null || !comment.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You do not have permission to update this comment.");
        }

        comment.setText(commentRequestDto.text);
        comment.setModifiedOn(Timestamp.from(Instant.now()));

        return CommentMapper.INSTANCE.commentToDto(comment);
    }

    @ShareableEntity(type= ShareableEntityType.ASSESSMENT, id = String.class)
    @Transactional
    public void deleteComment(String assessmentId, Long commentId, String userId ) {

        var comment = commentRepository.findById(commentId);

        if (comment == null || !comment.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You do not have permission to delete this comment.");
        }

        commentRepository.deleteById(commentId);

    }

    @Transactional
    public void deleteAll() {
        commentRepository.deleteAll();
    }

    @ShareableEntity(type= ShareableEntityType.ASSESSMENT, id = String.class)
    @Transactional
    public PageResource<CommentResponseDto> listComments(String assessmentId, int page, int size, UriInfo uriInfo) {

        var commentsPage = commentRepository.fetchCommentsByAssessmentId(assessmentId, page, size);
        var commentDTOs = CommentMapper.INSTANCE.commentsToDtos(commentsPage.list());

        return new PageResource<>(commentsPage, commentDTOs, uriInfo);
    }

}
