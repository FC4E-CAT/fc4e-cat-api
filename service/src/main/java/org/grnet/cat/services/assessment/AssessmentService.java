package org.grnet.cat.services.assessment;

import jakarta.ws.rs.core.UriInfo;
import org.grnet.cat.dtos.assessment.AssessmentRequest;
import org.grnet.cat.dtos.assessment.AssessmentResponse;
import org.grnet.cat.dtos.pagination.PageResource;

public interface AssessmentService <Request extends AssessmentRequest, Update, Response extends AssessmentResponse> {

    Response createAssessment(String userId, Request request);

    Response getAssessment(String userId, Long assessmentId);

    void deleteAll();

    Response updateAssessment(Long id, String userId, Update request);

    PageResource<Response> getAssessmentsByUserAndPage(int page, int size, UriInfo uriInfo, String userID);
}
