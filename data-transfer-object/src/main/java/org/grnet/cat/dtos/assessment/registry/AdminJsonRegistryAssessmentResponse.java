package org.grnet.cat.dtos.assessment.registry;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.dtos.assessment.AssessmentResponse;
import org.grnet.cat.enums.ZenodoState;

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

    @Schema(
            type = SchemaType.BOOLEAN,
            implementation = Boolean.class,
            description = "Indicates whether the assessment has been published on Zenodo.",
            example = "published"
    )
    @Setter
    @JsonProperty("zenodo_published")
    public Boolean zenodoPublished;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Zenodo deposit ID associated with this assessment, if published.",
            example = "1234567"
    )
    @Setter
    @JsonProperty("zenodo_deposit_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String zenodoDepositId;

    @Schema(
            type = SchemaType.STRING,
            implementation = String.class,
            description = "The Zenodo deposit url associated with this assessment, if published.",
            example = "https://zenodo.org/records/185555"
    )
    @Setter
    @JsonProperty("zenodo_deposit_url")
    @JsonInclude(JsonInclude.Include.NON_NULL)

    public String zenodoDepositUrl;

    @Schema(
            type = SchemaType.BOOLEAN,
            implementation = ZenodoState.class,
            description = "Indicates the state of publication of the assessment on Zenodo.",
            example = "IN PROGRESS"
    )
    @Setter
    @JsonProperty("zenodo_publication_state")
    public ZenodoState zenodoPublicationState;
}
