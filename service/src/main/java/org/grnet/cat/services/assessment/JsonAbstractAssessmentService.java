package org.grnet.cat.services.assessment;

import com.networknt.schema.SpecVersion;
import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException;
import org.grnet.cat.dtos.assessment.AssessmentResponse;
import org.grnet.cat.dtos.assessment.JsonAssessmentRequest;
import org.grnet.cat.dtos.template.TemplateDto;
import org.grnet.cat.entities.JsonSchema;
import org.grnet.cat.enums.ShareableEntityType;
import org.grnet.cat.services.interceptors.ShareableEntity;
import org.grnet.cat.utils.Utility;

public abstract class JsonAbstractAssessmentService<Request extends JsonAssessmentRequest, Update, Response extends AssessmentResponse, Entity> implements AssessmentService<Request , Update, Response, Entity> {

    @Inject
    Utility utility;

    /**
     * Deletes a private assessment if it is not published and belongs to the authenticated user.
     *
     * @param assessmentId The ID of the assessment to be deleted.
     * @throws ForbiddenException If the user does not have permission to delete this assessment (e.g., it's published or doesn't belong to them).
     */
    @ShareableEntity(type= ShareableEntityType.ASSESSMENT, id = String.class)
    public void deletePrivateAssessmentBelongsToUser(String assessmentId){

        deletePrivateAssessment(assessmentId);
    }

    /**
     * Updates the Assessment's json document belongs to the authenticated user.
     *
     * @param id The ID of the assessment is being updated.
     * @param request The update request.
     * @return The updated assessment
     * @throws ForbiddenException If the user does not have permission to update this assessment (e.g., it's published or doesn't belong to them).
     */
    @ShareableEntity(type= ShareableEntityType.ASSESSMENT, id = String.class)
    public Response updatePrivateAssessmentBelongsToUser(String id, Update request) {

        return update(id, request);
    }

    /**
     * Deletes a private assessment if it is not published.
     *
     * @param assessmentId The ID of the assessment to be deleted.
     * @throws ForbiddenException If the user does not have permission to delete this assessment (e.g., it's published).
     */
    public void deletePrivateAssessment(String assessmentId){

//        var assessment = getAssessment(assessmentId);
//        forbidActionsToPublicAssessment(assessment);
        delete(assessmentId);
    }

    public abstract void handleSubjectDatabaseId(String userId, Request request);

    public abstract void validateAssessmentAgainstTemplate(String jsonRequest, String jsonTemplate);

    public void validateTemplateJson(TemplateDto assessmentDoc){

        utility.validateTemplateJson(JsonSchema.fetchById("assessment_json_schema").getJsonSchema(), assessmentDoc, SpecVersion.VersionFlag.V7);
    }
}
