package org.grnet.cat.dtos.assessment.registry;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.dtos.assessment.AdminPartialJsonAssessmentResponse;
import org.grnet.cat.dtos.assessment.AssessmentResponse;
import org.grnet.cat.dtos.assessment.UserPartialJsonAssessmentResponse;

import java.util.List;

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

    @Setter
    @Schema(
            type = SchemaType.ARRAY,
            implementation = AdminJsonRegistryAssessmentResponse.class,
            description = "List of versions of this test."
    )
    @JsonProperty("versions")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<AdminJsonRegistryAssessmentResponse> adminVersions;
}
