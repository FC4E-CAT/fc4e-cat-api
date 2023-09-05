package org.grnet.cat.services.assessment;

import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.assessment.AssessmentResponse;
import org.grnet.cat.dtos.pagination.PageResource;

public interface AssessmentService <Request, Update, Response extends AssessmentResponse, Entity> {

    Response createAssessment(String userId, Request request);

    Response getAssessment(String userId, Long assessmentId);

    void deleteAll();

    Response updateAssessment(Long id, String userId, Update request);

    PageResource<? extends Response> getDtoAssessmentsByUserAndPage(int page, int size, UriInfo uriInfo, String userID, String subjectName, String subjectType, Long actorId);

    PageResource<? extends Response> getPublishedAssessmentsByTypeAndActorAndPage(int page, int size, Long typeId, Long actorId, UriInfo uriInfo);

    void deletePrivateAssessment(String userID, String assessmentId);
}
