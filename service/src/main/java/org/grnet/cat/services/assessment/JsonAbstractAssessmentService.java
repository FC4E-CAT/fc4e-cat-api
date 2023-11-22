package org.grnet.cat.services.assessment;

import jakarta.ws.rs.ForbiddenException;
import org.grnet.cat.dtos.assessment.AssessmentResponse;

public abstract class JsonAbstractAssessmentService<Request, Update, Response extends AssessmentResponse, Entity> implements AssessmentService<Request , Update, Response, Entity> {

    /**
     * Deletes a private assessment if it is not published and belongs to the authenticated user.
     *
     * @param userID The ID of the user who requests their assessments.
     * @param assessmentId The ID of the assessment to be deleted.
     * @throws ForbiddenException If the user does not have permission to delete this assessment (e.g., it's published or doesn't belong to them).
     */
    public void deletePrivateAssessmentBelongsToUser(String userID, String assessmentId){

        assessmentBelongsToUser(userID, assessmentId);
        deletePrivateAssessment(assessmentId);
    }

    /**
     * Updates the Assessment's json document belongs to the authenticated user.
     *
     * @param id The ID of the assessment is being updated.
     * @param userId The ID of the user who requests to update a specific assessment.
     * @param request The update request.
     * @return The updated assessment
     * @throws ForbiddenException If the user does not have permission to update this assessment (e.g., it's published or doesn't belong to them).
     */
    public Response updatePrivateAssessmentBelongsToUser(String id, String userId, Update request) {

        assessmentBelongsToUser(userId, id);
        return updatePrivateAssessment(id, request);
    }

    /**
     * Deletes a private assessment if it is not published.
     *
     * @param assessmentId The ID of the assessment to be deleted.
     * @throws ForbiddenException If the user does not have permission to delete this assessment (e.g., it's published).
     */
    public void deletePrivateAssessment(String assessmentId){

        var assessment = getAssessment(assessmentId);
        forbidActionsToPublicAssessment(assessment);
        delete(assessmentId);
    }

    /**
     * Updates a private assessment if it is not published.
     *
     * @param id The ID of the assessment whose is being updated.
     * @param request The update request.
     * @throws ForbiddenException If the user does not have permission to delete this assessment (e.g., it's published).
     */
    public Response updatePrivateAssessment(String id, Update request){

        var assessment = getAssessment(id);
        forbidActionsToPublicAssessment(assessment);
        return update(id, request);
    }
}
