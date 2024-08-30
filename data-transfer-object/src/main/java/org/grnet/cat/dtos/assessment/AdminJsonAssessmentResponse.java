package org.grnet.cat.dtos.assessment;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "AdminJsonAssessmentResponse", description = "This object represents the Admin Json Assessment.")
public class AdminJsonAssessmentResponse extends AssessmentResponse {

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "The template id for the assessment")
    @JsonProperty("template_id")
    public Long templateId;

    @Schema(
            type = SchemaType.OBJECT,
            implementation = AssessmentDoc.class,
            description = "The assessment doc")
    @JsonProperty("assessment_doc")
    public AssessmentDoc assessmentDoc;

    @Schema(
            type = SchemaType.BOOLEAN,
            implementation = Boolean.class,
            description = "Indicates that the assessment has been shared",
            example = "true"
    )
    @JsonProperty("shared")
    public Boolean shared;
}
