package org.grnet.cat.dtos.assessment.registry;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.dtos.assessment.AssessmentResponse;

@Schema(name = "AdminJsonAssessmentV2Response", description = "This object represents the Admin Registry Assessment Json Assessment.")
public class AdminJsonRegistryAssessmentResponse extends AssessmentResponse {

    @Schema(
            type = SchemaType.OBJECT,
            implementation = RegistryAssessmentDto.class,
            description = "The assessment doc")
    @JsonProperty("assessment_doc")
    public RegistryAssessmentDto assessmentDoc;

    @Schema(
            type = SchemaType.BOOLEAN,
            implementation = Boolean.class,
            description = "Indicates that the assessment has been shared",
            example = "true"
    )
    @JsonProperty("shared")
    public Boolean shared;
}
