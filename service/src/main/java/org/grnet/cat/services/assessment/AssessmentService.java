package org.grnet.cat.services.assessment;

import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.assessment.AssessmentResponse;
import org.grnet.cat.dtos.pagination.PageResource;
import org.grnet.cat.entities.Assessment;

public interface AssessmentService <Request extends AssessmentRequest, Update, Response extends AssessmentResponse, Entity> {

    Response createAssessment(String userId, Request request);

    Response getDtoAssessment(String userId, String id);

    void deleteAll();

    Response update(String id, Update request);

    PageResource<? extends Response> getDtoAssessmentsByUserAndPage(int page, int size, UriInfo uriInfo, String userID);

    PageResource<? extends Response> getPublishedDtoAssessmentsByTypeAndActorAndPage(int page, int size, Long typeId, Long actorId, UriInfo uriInfo);

    void assessmentBelongsToUser(String userID, String id);

    Entity getAssessment(String id);

    void forbidActionsToPublicAssessment(Entity assessment);

    void delete(String id);
}
