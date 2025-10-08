package org.grnet.cat.dtos.assessment.registry;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.grnet.cat.dtos.assessment.AssessmentResponse;

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
            description = "The Zenodo file url associated with this assessment, if published.",
            example = "https://sandbox.zenodo.org/api/records/185555/draft/files/69ef9a51-09c1-48f8-920f-580d58552e84/"
    )
    @Setter
    @JsonProperty("zenodo_file_url")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String zenodoFileUrl;


}
