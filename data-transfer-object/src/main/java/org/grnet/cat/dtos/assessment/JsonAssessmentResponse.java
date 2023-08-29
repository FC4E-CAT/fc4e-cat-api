package org.grnet.cat.dtos.assessment;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "JsonAssessmentResponse", description = "This object represents the Json Assessment.")
public class JsonAssessmentResponse extends AssessmentResponse {

    @Schema(
            type = SchemaType.NUMBER,
            implementation = Long.class,
            description = "The template id for the assessment")
    @JsonProperty("templateId")
    public Long templateId;

    @Schema(
            type = SchemaType.OBJECT,
            implementation = AssessmentDoc.class,
            description = "The assessment doc")
    @JsonProperty("assessmentDoc")
    public AssessmentDoc assessmentDoc;
}
